import { screen, waitFor, fireEvent, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { renderWith } from '../helpers.jsx'

vi.mock('../../api/index.js', () => ({
  listCourses:  vi.fn(),
  enroll:       vi.fn(),
  drop:         vi.fn(),
  createCourse: vi.fn(),
}))

const authState = vi.hoisted(() => ({
  auth: { username: 'eve', role: 'Student', isResearcher: false, token: 'tok' },
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
import { Courses } from '../../pages/Courses.jsx'

const COURSES = [
  { id: 'CRS-1', name: 'Math',   credits: 5, type: 'MAJOR', capacity: 50, remainingSeats: 48, isFull: false, teachers: ['bob'], students: [] },
  { id: 'CRS-2', name: 'OOP',    credits: 5, type: 'MAJOR', capacity: 30, remainingSeats: 0,  isFull: true,  teachers: ['bob'], students: [] },
  { id: 'CRS-3', name: 'French', credits: 2, type: 'FREE',  capacity: 25, remainingSeats: 24, isFull: false, teachers: [],      students: [] },
]

beforeEach(() => {
  authState.auth = { username: 'eve', role: 'Student', isResearcher: false, token: 'tok' }
  api.listCourses.mockResolvedValue(COURSES)
  api.enroll.mockResolvedValue({ message: 'Enrolled.' })
  api.drop.mockResolvedValue({ message: 'Dropped.' })
  api.createCourse.mockResolvedValue({ id: 'CRS-4', name: 'History', credits: 3, type: 'MINOR' })
})

function render() { return renderWith(<Courses />) }

test('renders all courses', async () => {
  render()
  await waitFor(() => expect(screen.getByText('Math')).toBeInTheDocument())
  expect(screen.getByText('OOP')).toBeInTheDocument()
  expect(screen.getByText('French')).toBeInTheDocument()
})

test('full course shows FULL badge', async () => {
  render()
  await waitFor(() => screen.getByText('OOP'))
  expect(screen.getByText('FULL')).toBeInTheDocument()
})

test('Enroll button calls api.enroll with courseId', async () => {
  render()
  await waitFor(() => screen.getAllByText('Enroll'))
  await userEvent.click(screen.getAllByText('Enroll')[0])
  expect(api.enroll).toHaveBeenCalledWith('CRS-1')
})

test('Drop button calls api.drop with courseId', async () => {
  render()
  await waitFor(() => screen.getAllByText('Drop'))
  await userEvent.click(screen.getAllByText('Drop')[0])
  expect(api.drop).toHaveBeenCalledWith('CRS-1')
})

test('manager sees New Course button; student does not', async () => {
  authState.auth = { username: 'dave', role: 'Manager', isResearcher: false, token: 'tok' }
  render()
  await waitFor(() => expect(screen.getByText('＋ New Course')).toBeInTheDocument())
})

test('student does NOT see New Course button', async () => {
  render()
  await waitFor(() => screen.getByText('Math'))
  expect(screen.queryByText('＋ New Course')).not.toBeInTheDocument()
})

test('create course form sends name to api.createCourse', async () => {
  authState.auth = { username: 'dave', role: 'Manager', isResearcher: false, token: 'tok' }
  render()
  await waitFor(() => screen.getByText('＋ New Course'))
  await userEvent.click(screen.getByText('＋ New Course'))
  // Course Name input (first textbox in the modal)
  await waitFor(() => document.querySelector('#create-course-form'))
  const nameInput = within(document.querySelector('#create-course-form')).getAllByRole('textbox')[0]
  await userEvent.type(nameInput, 'History')
  fireEvent.submit(document.querySelector('#create-course-form'))
  await waitFor(() =>
    expect(api.createCourse).toHaveBeenCalledWith(
      expect.objectContaining({ name: 'History' })
    )
  )
})

test('search filters courses by name', async () => {
  render()
  await waitFor(() => screen.getByText('Math'))
  await userEvent.type(screen.getByPlaceholderText('Search courses…'), 'OOP')
  expect(screen.queryByText('Math')).not.toBeInTheDocument()
  expect(screen.getByText('OOP')).toBeInTheDocument()
})
