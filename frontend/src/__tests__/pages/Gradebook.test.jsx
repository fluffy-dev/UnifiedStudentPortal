import { screen, waitFor, fireEvent, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { renderWith } from '../helpers.jsx'

vi.mock('../../api/index.js', () => ({
  listCourses:   vi.fn(),
  viewGrades:    vi.fn(),
  recordMarks:   vi.fn(),
  userDirectory: vi.fn(),
}))

const authState = vi.hoisted(() => ({
  auth: { username: 'bob', role: 'Teacher', isResearcher: false, token: 'tok' },
}))

vi.mock('../../context/AuthContext.jsx', () => ({
  useAuth: () => ({ ...authState, isAuth: true, signIn: vi.fn(), signOut: vi.fn(), updateResearcherStatus: vi.fn() }),
  AuthProvider: ({ children }) => children,
}))

vi.mock('../../context/I18nContext.jsx', async () => {
  const { makeT, BUNDLE } = await import('../helpers.jsx')
  return {
    useI18n: () => ({ t: makeT(BUNDLE), language: 'en', changeLanguage: vi.fn(), loading: false }),
    I18nProvider: ({ children }) => children,
  }
})

import * as api from '../../api/index.js'
import { Gradebook } from '../../pages/Gradebook.jsx'

const COURSES = [
  { id: 'CRS-1', name: 'Math',    credits: 5, type: 'MAJOR', capacity: 50, remainingSeats: 48, isFull: false, teachers: ['bob'], students: ['eve', 'leo'] },
  { id: 'CRS-2', name: 'OOP',     credits: 5, type: 'MAJOR', capacity: 30, remainingSeats: 28, isFull: false, teachers: ['bob', 'alice'], students: ['leo'] },
  { id: 'CRS-3', name: 'Physics', credits: 4, type: 'MINOR', capacity: 20, remainingSeats: 20, isFull: false, teachers: ['carol'], students: [] },
]

const GRADES = [
  { student: 'eve', firstHalf: 25, secondHalf: 28, exam: 35, attestationTotal: 53, total: 88, letter: 'B', admittedToExam: true, passing: true },
  { student: 'leo', firstHalf: 10, secondHalf: 10, exam: 40, attestationTotal: 20, total: 60, letter: 'F', admittedToExam: false, passing: false },
]

const DIRECTORY = [
  { username: 'eve', fullName: 'Eve Smith', role: 'Student' },
  { username: 'leo', fullName: 'Leo Park',  role: 'Student' },
]

beforeEach(() => {
  authState.auth = { username: 'bob', role: 'Teacher', isResearcher: false, token: 'tok' }
  api.listCourses.mockResolvedValue(COURSES)
  api.viewGrades.mockResolvedValue(GRADES)
  api.recordMarks.mockResolvedValue({ message: 'Marks recorded.' })
  api.userDirectory.mockResolvedValue(DIRECTORY)
})

function render() { return renderWith(<Gradebook />) }

test('teacher sees only their assigned courses (not Physics)', async () => {
  render()
  await waitFor(() => screen.getByText('Math'))
  expect(screen.getByText('OOP')).toBeInTheDocument()
  expect(screen.queryByText('Physics')).not.toBeInTheDocument()
})

test('dean sees all courses', async () => {
  authState.auth = { username: 'carol', role: 'Dean', isResearcher: false, token: 'tok' }
  render()
  await waitFor(() => screen.getByText('Physics'))
  expect(screen.getByText('Math')).toBeInTheDocument()
})

test('clicking a course loads grades', async () => {
  render()
  await waitFor(() => screen.getByText('Math'))
  await userEvent.click(screen.getByText('Math'))
  await waitFor(() => expect(api.viewGrades).toHaveBeenCalledWith('CRS-1'))
  expect(screen.getByText('Eve Smith')).toBeInTheDocument()
})

test('student with attestation < 30 shows em-dash in exam column', async () => {
  render()
  await waitFor(() => screen.getByText('Math'))
  await userEvent.click(screen.getByText('Math'))
  await waitFor(() => screen.getByText('Leo Park'))
  const leoRow = screen.getByText('Leo Park').closest('tr')
  expect(leoRow?.textContent).toContain('—')
})

test('record marks form calls api.recordMarks with correct fields', async () => {
  render()
  await waitFor(() => screen.getByText('Math'))
  await userEvent.click(screen.getByText('Math'))
  await waitFor(() => screen.getByText('＋ Record Marks'))
  await userEvent.click(screen.getByText('＋ Record Marks'))

  await waitFor(() => screen.getByPlaceholderText('Search users…'))
  await userEvent.type(screen.getByPlaceholderText('Search users…'), 'Eve')
  await waitFor(() => document.querySelector('.user-picker-menu'))
  await userEvent.click(within(document.querySelector('.user-picker-menu')).getByText('Eve Smith'))

  const spinners = screen.getAllByRole('spinbutton')
  await userEvent.clear(spinners[0]); await userEvent.type(spinners[0], '25')
  await userEvent.clear(spinners[1]); await userEvent.type(spinners[1], '28')
  await userEvent.clear(spinners[2]); await userEvent.type(spinners[2], '35')
  fireEvent.submit(document.querySelector('#marks-form'))

  await waitFor(() =>
    expect(api.recordMarks).toHaveBeenCalledWith('CRS-1', {
      studentUsername: 'eve', firstHalf: 25, secondHalf: 28, exam: 35,
    })
  )
})

test('course search filters list', async () => {
  render()
  await waitFor(() => screen.getByText('Math'))
  await userEvent.type(screen.getByPlaceholderText('Search courses…'), 'OOP')
  expect(screen.queryByText('Math')).not.toBeInTheDocument()
  expect(screen.getByText('OOP')).toBeInTheDocument()
})
