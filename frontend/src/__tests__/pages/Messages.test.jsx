import { screen, waitFor, fireEvent, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { renderWith } from '../helpers.jsx'

vi.mock('../../api/index.js', () => ({
  inbox:         vi.fn(),
  sendMessage:   vi.fn(),
  userDirectory: vi.fn(),
}))

vi.mock('../../context/AuthContext.jsx', () => ({
  useAuth: () => ({
    auth: { username: 'eve', role: 'Student', isResearcher: false, token: 'tok' },
    isAuth: true, signIn: vi.fn(), signOut: vi.fn(), updateResearcherStatus: vi.fn(),
  }),
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
import { Messages } from '../../pages/Messages.jsx'

const MSGS = [
  { id: 1, sender: 'bob', senderFullName: 'Bob Ross', recipient: 'eve',
    subject: 'Office hours', body: 'Tue 14-16', urgency: 'LOW', status: 'UNREAD',
    sentAt: '2026-01-15T09:00:00' },
  { id: 2, sender: 'dave', senderFullName: 'Dave Johnson', recipient: 'eve',
    subject: 'Request approved', body: 'Approved.', urgency: 'MEDIUM', status: 'READ',
    sentAt: '2026-01-16T10:00:00' },
]

const DIR = [
  { username: 'bob',  fullName: 'Bob Ross',    role: 'Teacher' },
  { username: 'dave', fullName: 'Dave Johnson', role: 'Manager' },
]

beforeEach(() => {
  api.inbox.mockResolvedValue(MSGS)
  api.sendMessage.mockResolvedValue({ message: 'Message sent.' })
  api.userDirectory.mockResolvedValue(DIR)
})

function render() { return renderWith(<Messages />) }

test('renders inbox with subjects', async () => {
  render()
  await waitFor(() => expect(screen.getByText('Office hours')).toBeInTheDocument())
  expect(screen.getByText('Request approved')).toBeInTheDocument()
})

test('shows sender full name with @handle', async () => {
  render()
  await waitFor(() => screen.getByText('Bob Ross'))
  expect(screen.getByText('@bob')).toBeInTheDocument()
})

test('LOW urgency badge has badge-gray class', async () => {
  render()
  await waitFor(() => screen.getByText('Low'))
  expect(screen.getByText('Low')).toHaveClass('badge-gray')
})

test('search filters by subject', async () => {
  render()
  await waitFor(() => screen.getByText('Office hours'))
  const searchInput = screen.getByPlaceholderText('Search messages…')
  await userEvent.type(searchInput, 'approved')
  expect(screen.queryByText('Office hours')).not.toBeInTheDocument()
  expect(screen.getByText('Request approved')).toBeInTheDocument()
})

test('compose sends correct body to api.sendMessage', async () => {
  render()
  await waitFor(() => screen.getByText('✉️ Compose'))
  await userEvent.click(screen.getByText('✉️ Compose'))
  // recipient picker
  const recipientInput = screen.getByPlaceholderText('To (username)')
  await userEvent.type(recipientInput, 'bob')
  await waitFor(() => document.querySelector('.user-picker-menu'))
  // click Bob Ross inside the picker dropdown (not the one in the inbox table)
  await userEvent.click(within(document.querySelector('.user-picker-menu')).getByText('Bob Ross'))
  // formInputs: [0]=UserPicker input (already has 'bob'), [1]=Subject, [2]=Textarea body
  const formInputs = document.querySelectorAll('#compose-form input, #compose-form textarea')
  await userEvent.type(formInputs[1], 'Hello!')   // subject
  await userEvent.type(formInputs[2], 'Hey there') // body
  fireEvent.submit(document.querySelector('#compose-form'))
  await waitFor(() =>
    expect(api.sendMessage).toHaveBeenCalledWith(
      expect.objectContaining({ recipient: 'bob' })
    )
  )
})

test('empty inbox shows empty state', async () => {
  api.inbox.mockResolvedValue([])
  render()
  await waitFor(() => expect(screen.getByText('Your inbox is empty.')).toBeInTheDocument())
})
