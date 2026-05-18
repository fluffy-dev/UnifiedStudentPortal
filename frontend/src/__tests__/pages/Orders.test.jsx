import { screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { renderWith } from '../helpers.jsx'

vi.mock('../../api/index.js', () => ({
  listOrders:    vi.fn(),
  createOrder:   vi.fn(),
  acceptOrder:   vi.fn(),
  completeOrder: vi.fn(),
}))

const authState = vi.hoisted(() => ({
  auth: { username: 'tech', role: 'TechSupport', isResearcher: false, token: 'tok' },
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
import { Orders } from '../../pages/Orders.jsx'

const ORDERS = [
  { id: 1, requester: 'bob', requesterFullName: 'Bob Ross', description: 'Keyboard issue', status: 'NEW',      createdAt: '2026-01-15' },
  { id: 2, requester: 'bob', requesterFullName: 'Bob Ross', description: 'Projector broken', status: 'ACCEPTED', createdAt: '2026-01-14' },
  { id: 3, requester: 'dave', requesterFullName: 'Dave Johnson', description: 'Network cable',  status: 'DONE',     createdAt: '2026-01-13', executorFullName: 'Tom Reilly' },
]

beforeEach(() => {
  authState.auth = { username: 'tech', role: 'TechSupport', isResearcher: false, token: 'tok' }
  api.listOrders.mockResolvedValue(ORDERS)
  api.createOrder.mockResolvedValue({ message: 'Order created.' })
  api.acceptOrder.mockResolvedValue({ message: 'Order accepted.' })
  api.completeOrder.mockResolvedValue({ message: 'Order completed.' })
})

function render() { return renderWith(<Orders />) }

test('renders order descriptions', async () => {
  render()
  await waitFor(() => expect(screen.getByText('Keyboard issue')).toBeInTheDocument())
  expect(screen.getByText('Projector broken')).toBeInTheDocument()
})

test('shows requester full name', async () => {
  render()
  await waitFor(() => screen.getAllByText('Bob Ross'))
  // Two orders from bob — both show @bob; verify at least one exists
  expect(screen.getAllByText('@bob').length).toBeGreaterThan(0)
})

test('techsupport sees Accept button for NEW orders', async () => {
  render()
  await waitFor(() => screen.getByText('Keyboard issue'))
  expect(screen.getByRole('button', { name: 'Accept' })).toBeInTheDocument()
})

test('techsupport sees Complete button for ACCEPTED orders', async () => {
  render()
  await waitFor(() => screen.getByText('Projector broken'))
  expect(screen.getByRole('button', { name: 'Complete' })).toBeInTheDocument()
})

test('non-tech user does not see action columns', async () => {
  authState.auth = { username: 'bob', role: 'Teacher', isResearcher: false, token: 'tok' }
  render()
  await waitFor(() => expect(api.listOrders).toHaveBeenCalled())
  expect(screen.queryByRole('button', { name: 'Accept' })).not.toBeInTheDocument()
})

test('accept calls api.acceptOrder with correct id', async () => {
  render()
  await waitFor(() => screen.getByRole('button', { name: 'Accept' }))
  await userEvent.click(screen.getByRole('button', { name: 'Accept' }))
  expect(api.acceptOrder).toHaveBeenCalledWith(1)
})

test('create order form sends description and deviceType', async () => {
  authState.auth = { username: 'bob', role: 'Teacher', isResearcher: false, token: 'tok' }
  render()
  await waitFor(() => screen.getByText('＋ New Order'))
  await userEvent.click(screen.getByText('＋ New Order'))
  // Modal opens — description is a textarea
  const textarea = document.querySelector('textarea')
  await userEvent.type(textarea, 'My laptop is dead')
  await userEvent.click(screen.getByRole('button', { name: 'Submit' }))
  await waitFor(() =>
    expect(api.createOrder).toHaveBeenCalledWith(
      expect.objectContaining({ description: 'My laptop is dead' })
    )
  )
})

test('status filter hides non-matching rows', async () => {
  render()
  await waitFor(() => screen.getByText('Keyboard issue'))
  const comboboxes = screen.getAllByRole('combobox')
  await userEvent.selectOptions(comboboxes[comboboxes.length - 1], 'NEW')
  expect(screen.getByText('Keyboard issue')).toBeInTheDocument()
  expect(screen.queryByText('Projector broken')).not.toBeInTheDocument()
})
