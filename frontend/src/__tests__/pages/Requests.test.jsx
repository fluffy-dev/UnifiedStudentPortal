import { screen, waitFor, fireEvent, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { renderWith, t } from '../helpers.jsx'

vi.mock('../../api/index.js', () => ({
  listRequests:   vi.fn(),
  submitRequest:  vi.fn(),
  processRequest: vi.fn(),
}))

const authState = vi.hoisted(() => ({
  auth: { username: 'eve', role: 'Student', isResearcher: false, token: 'tok' },
  isAuth: true,
}))

vi.mock('../../context/AuthContext.jsx', () => ({
  useAuth: () => ({ ...authState, signIn: vi.fn(), signOut: vi.fn(), updateResearcherStatus: vi.fn() }),
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
import { Requests } from '../../pages/Requests.jsx'

const SAMPLE = [
  { id: 1, requester: 'eve', requesterFullName: 'Eve Smith', title: 'Need transcript',
    type: 'TRANSCRIPT_FOR_SEMESTER', urgency: 'HIGH', status: 'PENDING',
    body: 'For visa purposes', createdAt: '2026-01-15T10:00:00' },
  { id: 2, requester: 'leo', requesterFullName: 'Leo Park', title: 'Certificate',
    type: 'CERTIFICATE_OF_EDUCATION', urgency: 'LOW', status: 'APPROVED',
    body: 'For job application', createdAt: '2026-01-16T10:00:00' },
]

beforeEach(() => {
  authState.auth = { username: 'eve', role: 'Student', isResearcher: false, token: 'tok' }
  api.listRequests.mockResolvedValue(SAMPLE)
  api.submitRequest.mockResolvedValue({ message: 'Request submitted' })
  api.processRequest.mockResolvedValue({ message: 'Request APPROVED' })
})

function render() { return renderWith(<Requests />) }

test('renders request list with titles', async () => {
  render()
  await waitFor(() => expect(screen.getByText('Need transcript')).toBeInTheDocument())
  expect(screen.getByText('Certificate')).toBeInTheDocument()
})

test('shows requester full name', async () => {
  render()
  await waitFor(() => expect(screen.getByText('Eve Smith')).toBeInTheDocument())
})

test('expand/collapse shows request body', async () => {
  render()
  await waitFor(() => screen.getByText('Need transcript'))
  await userEvent.click(screen.getAllByText('Read more')[0])
  expect(screen.getByText('For visa purposes')).toBeInTheDocument()
  await userEvent.click(screen.getByText('Collapse'))
  expect(screen.queryByText('For visa purposes')).not.toBeInTheDocument()
})

test('status filter hides non-matching requests', async () => {
  render()
  await waitFor(() => screen.getByText('Need transcript'))
  const selects = screen.getAllByRole('combobox')
  await userEvent.selectOptions(selects[selects.length - 1], 'PENDING')
  expect(screen.getByText('Need transcript')).toBeInTheDocument()
  expect(screen.queryByText('Certificate')).not.toBeInTheDocument()
})

test('search filters by title', async () => {
  render()
  await waitFor(() => screen.getByText('Need transcript'))
  await userEvent.type(screen.getByPlaceholderText('Search requests…'), 'cert')
  expect(screen.queryByText('Need transcript')).not.toBeInTheDocument()
  expect(screen.getByText('Certificate')).toBeInTheDocument()
})

test('submit form calls api.submitRequest with all required fields', async () => {
  api.listRequests.mockResolvedValue([])
  render()
  await waitFor(() => screen.getByText(/New Request/))
  await userEvent.click(screen.getByText(/New Request/))
  // fill title inside the form (not the page-level search bar)
  await waitFor(() => document.querySelector('#req-form'))
  const titleInput = within(document.querySelector('#req-form')).getAllByRole('textbox')[0]
  await userEvent.type(titleInput, 'My request title')
  fireEvent.submit(document.querySelector('#req-form'))
  await waitFor(() =>
    expect(api.submitRequest).toHaveBeenCalledWith(
      expect.objectContaining({ title: 'My request title', type: expect.any(String) })
    )
  )
})

test('manager sees Approve/Reject buttons on PENDING requests', async () => {
  authState.auth = { username: 'dave', role: 'Manager', isResearcher: false, token: 'tok' }
  render()
  await waitFor(() => screen.getByText('Need transcript'))
  expect(screen.getByText('✓ Approve')).toBeInTheDocument()
  expect(screen.getByText('✗ Reject')).toBeInTheDocument()
})

test('student does NOT see Approve/Reject buttons', async () => {
  render()
  await waitFor(() => screen.getByText('Need transcript'))
  expect(screen.queryByText('✓ Approve')).not.toBeInTheDocument()
})

test('clicking Approve calls processRequest with APPROVED', async () => {
  authState.auth = { username: 'dave', role: 'Manager', isResearcher: false, token: 'tok' }
  render()
  await waitFor(() => screen.getByText('✓ Approve'))
  await userEvent.click(screen.getByText('✓ Approve'))
  expect(api.processRequest).toHaveBeenCalledWith(1, 'APPROVED')
})

test('empty state shown when no requests', async () => {
  api.listRequests.mockResolvedValue([])
  render()
  await waitFor(() => expect(screen.getByText('No requests')).toBeInTheDocument())
})
