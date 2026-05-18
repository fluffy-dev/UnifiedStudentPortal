import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { UserPicker } from '../../components/UserPicker.jsx'

const USERS = [
  { username: 'alice', fullName: 'Alice Walker', role: 'Teacher' },
  { username: 'bob',   fullName: 'Bob Ross',     role: 'Teacher' },
  { username: 'eve',   fullName: 'Eve Smith',    role: 'Student' },
]

function setup(overrides = {}) {
  const onChange = vi.fn()
  render(
    <UserPicker
      users={USERS}
      value=""
      onChange={onChange}
      placeholder="Pick user"
      {...overrides}
    />
  )
  return { onChange }
}

test('renders the input', () => {
  setup()
  expect(screen.getByPlaceholderText('Pick user')).toBeInTheDocument()
})

test('shows dropdown options when focused', async () => {
  setup()
  await userEvent.click(screen.getByPlaceholderText('Pick user'))
  expect(screen.getByText('Alice Walker')).toBeInTheDocument()
  expect(screen.getByText('Bob Ross')).toBeInTheDocument()
})

test('filters by fullName prefix', async () => {
  setup()
  await userEvent.type(screen.getByPlaceholderText('Pick user'), 'Alice')
  expect(screen.getByText('Alice Walker')).toBeInTheDocument()
  expect(screen.queryByText('Bob Ross')).not.toBeInTheDocument()
})

test('filters by username', async () => {
  setup()
  await userEvent.type(screen.getByPlaceholderText('Pick user'), 'bob')
  expect(screen.getByText('Bob Ross')).toBeInTheDocument()
  expect(screen.queryByText('Alice Walker')).not.toBeInTheDocument()
})

test('clicking a suggestion calls onChange with username', async () => {
  const { onChange } = setup()
  await userEvent.click(screen.getByPlaceholderText('Pick user'))
  await userEvent.click(screen.getByText('Alice Walker'))
  expect(onChange).toHaveBeenCalledWith('alice')
})

test('filters by role when roles prop provided', async () => {
  setup({ roles: ['Teacher'] })
  await userEvent.click(screen.getByPlaceholderText('Pick user'))
  expect(screen.getByText('Alice Walker')).toBeInTheDocument()
  expect(screen.getByText('Bob Ross')).toBeInTheDocument()
  expect(screen.queryByText('Eve Smith')).not.toBeInTheDocument()
})

test('shows @username and role in suggestion', async () => {
  setup()
  await userEvent.click(screen.getByPlaceholderText('Pick user'))
  expect(screen.getByText('@alice · Teacher')).toBeInTheDocument()
})
