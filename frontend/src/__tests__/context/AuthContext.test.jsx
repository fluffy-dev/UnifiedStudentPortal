import { render, screen, waitFor, fireEvent } from '@testing-library/react'
import { AuthProvider, useAuth } from '../../context/AuthContext.jsx'

function Probe() {
  const { auth, signIn, signOut, updateResearcherStatus } = useAuth()
  return (
    <div>
      <span data-testid="auth">{auth ? 'yes' : 'no'}</span>
      <span data-testid="user">{auth?.username ?? 'none'}</span>
      <span data-testid="role">{auth?.role ?? 'none'}</span>
      <span data-testid="researcher">{String(auth?.isResearcher ?? false)}</span>
      <button onClick={() => signIn('tok', 'alice', 'Student', false)}>login</button>
      <button onClick={signOut}>logout</button>
      <button onClick={() => updateResearcherStatus(true)}>unlock</button>
    </div>
  )
}

const setup = () => render(<AuthProvider><Probe /></AuthProvider>)

test('starts unauthenticated', () => {
  setup()
  expect(screen.getByTestId('auth')).toHaveTextContent('no')
  expect(screen.getByTestId('user')).toHaveTextContent('none')
})

test('signIn sets auth state and persists token to localStorage', async () => {
  setup()
  fireEvent.click(screen.getByText('login'))
  await waitFor(() => expect(screen.getByTestId('auth')).toHaveTextContent('yes'))
  expect(screen.getByTestId('user')).toHaveTextContent('alice')
  expect(screen.getByTestId('role')).toHaveTextContent('Student')
  expect(localStorage.getItem('token')).toBe('tok')
  expect(localStorage.getItem('username')).toBe('alice')
})

test('signOut clears auth and localStorage', async () => {
  setup()
  fireEvent.click(screen.getByText('login'))
  await waitFor(() => expect(screen.getByTestId('auth')).toHaveTextContent('yes'))
  fireEvent.click(screen.getByText('logout'))
  await waitFor(() => expect(screen.getByTestId('auth')).toHaveTextContent('no'))
  expect(localStorage.getItem('token')).toBeNull()
})

test('updateResearcherStatus flips isResearcher', async () => {
  setup()
  fireEvent.click(screen.getByText('login'))
  await waitFor(() => expect(screen.getByTestId('researcher')).toHaveTextContent('false'))
  fireEvent.click(screen.getByText('unlock'))
  await waitFor(() => expect(screen.getByTestId('researcher')).toHaveTextContent('true'))
})

test('auth restores from localStorage on mount', async () => {
  localStorage.setItem('token', 'saved')
  localStorage.setItem('username', 'bob')
  localStorage.setItem('role', 'Teacher')
  localStorage.setItem('isResearcher', 'false')
  setup()
  await waitFor(() => expect(screen.getByTestId('auth')).toHaveTextContent('yes'))
  expect(screen.getByTestId('user')).toHaveTextContent('bob')
  expect(screen.getByTestId('role')).toHaveTextContent('Teacher')
})
