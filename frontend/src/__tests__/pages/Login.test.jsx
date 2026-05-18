import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { MemoryRouter } from 'react-router-dom'

vi.mock('../../api/index.js', () => ({
  login: vi.fn(),
}))

vi.mock('../../context/I18nContext.jsx', async () => {
  const { makeT, BUNDLE } = await import('../helpers.jsx')
  return {
    useI18n: () => ({ t: makeT(BUNDLE), language: 'en', changeLanguage: vi.fn(), loading: false }),
    I18nProvider: ({ children }) => children,
  }
})

vi.mock('../../context/AuthContext.jsx', () => ({
  useAuth: () => ({
    auth: null, isAuth: false,
    signIn: vi.fn(), signOut: vi.fn(), updateResearcherStatus: vi.fn(),
  }),
  AuthProvider: ({ children }) => children,
}))

import * as api from '../../api/index.js'
import { Login } from '../../pages/Login.jsx'

function setup() {
  return render(
    <MemoryRouter>
      <Login />
    </MemoryRouter>
  )
}

test('renders username and password fields', async () => {
  setup()
  await waitFor(() => expect(screen.getByText('Sign in')).toBeInTheDocument())
  const allInputs = document.querySelectorAll('input')
  expect(allInputs.length).toBeGreaterThanOrEqual(2)
})

test('calls api.login with entered username and password', async () => {
  api.login.mockResolvedValue({ token: 'tok', username: 'alice', role: 'Student', isResearcher: false })
  setup()
  await waitFor(() => screen.getByText('University System'))
  const [usernameInput, passwordInput] = document.querySelectorAll('input')
  await userEvent.type(usernameInput, 'alice')
  await userEvent.type(passwordInput, 'pass123')
  await userEvent.click(screen.getByRole('button', { name: /sign in/i }))
  await waitFor(() => expect(api.login).toHaveBeenCalledWith('alice', 'pass123'))
})

test('shows error message on login failure', async () => {
  api.login.mockRejectedValue({ message: 'Invalid credentials.' })
  setup()
  await waitFor(() => screen.getByText('University System'))
  const inputs = document.querySelectorAll('input')
  await userEvent.type(inputs[0], 'bad')
  await userEvent.type(inputs[1], 'wrong')
  await userEvent.click(screen.getByRole('button', { name: /sign in/i }))
  await waitFor(() => expect(screen.getByText('Invalid credentials.')).toBeInTheDocument())
})

test('shows demo accounts hint', async () => {
  setup()
  await waitFor(() => expect(screen.getByText('Demo accounts')).toBeInTheDocument())
})
