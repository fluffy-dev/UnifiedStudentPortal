import { screen, waitFor, fireEvent, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { renderWith } from '../helpers.jsx'

vi.mock('../../api/index.js', () => ({
  listNews:      vi.fn(),
  publishNews:   vi.fn(),
  commentOnNews: vi.fn(),
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
import { News } from '../../pages/News.jsx'

const SHORT = { id: 1, title: 'Semester start', body: 'Short body', author: 'bob', authorFullName: 'Bob Ross', publishedAt: '2026-01-10T09:00:00', pinned: false, comments: [] }
const PINNED = { ...SHORT, id: 2, title: 'Important Announcement', pinned: true }
const LONG   = { ...SHORT, id: 3, title: 'Long post', body: 'A'.repeat(300) }
const WITH_COMMENT = { ...SHORT, id: 4, title: 'With comment', comments: [
  { author: 'leo', authorFullName: 'Leo Park', text: 'Great!', postedAt: '2026-01-11T10:00:00' }
]}

beforeEach(() => {
  authState.auth = { username: 'eve', role: 'Student', isResearcher: false, token: 'tok' }
  api.listNews.mockResolvedValue([SHORT, PINNED])
  api.publishNews.mockResolvedValue({ message: 'News published.' })
  api.commentOnNews.mockResolvedValue({ message: 'Comment added.' })
})

function render() { return renderWith(<News />) }

test('renders news titles', async () => {
  render()
  await waitFor(() => expect(screen.getByText('Semester start')).toBeInTheDocument())
})

test('pinned news appears under Pinned heading', async () => {
  render()
  await waitFor(() => screen.getByText('Important Announcement'))
  // The pinned section renders a heading that includes "📌 Pinned"
  // and the badge says "Pinned" — check the section-title element
  const sections = document.querySelectorAll('.section-title')
  const pinnedSection = Array.from(sections).find(el => el.textContent.includes('Pinned'))
  expect(pinnedSection).toBeTruthy()
  expect(screen.getByText('Important Announcement')).toBeInTheDocument()
})

test('shows author full name', async () => {
  render()
  await waitFor(() => expect(screen.getAllByText(/Bob Ross/).length).toBeGreaterThan(0))
})

test('long body has Read more button', async () => {
  api.listNews.mockResolvedValue([LONG])
  render()
  await waitFor(() => screen.getByText('Long post'))
  expect(screen.getByText('Read more')).toBeInTheDocument()
})

test('Read more / Collapse toggles body visibility', async () => {
  api.listNews.mockResolvedValue([LONG])
  render()
  await waitFor(() => screen.getByText('Read more'))
  await userEvent.click(screen.getByText('Read more'))
  expect(screen.getByText('Collapse')).toBeInTheDocument()
  await userEvent.click(screen.getByText('Collapse'))
  expect(screen.queryByText('Collapse')).not.toBeInTheDocument()
})

test('comment form sends { comment } to api.commentOnNews', async () => {
  api.listNews.mockResolvedValue([SHORT])
  render()
  await waitFor(() => screen.getAllByText('💬 Comment'))
  await userEvent.click(screen.getAllByText('💬 Comment')[0])
  await userEvent.type(screen.getByPlaceholderText('Write your comment…'), 'Nice post!')
  await userEvent.click(screen.getByRole('button', { name: 'Post' }))
  expect(api.commentOnNews).toHaveBeenCalledWith(1, 'Nice post!')
})

test('employee sees Publish button; student does not', async () => {
  authState.auth = { username: 'dave', role: 'Manager', isResearcher: false, token: 'tok' }
  api.listNews.mockResolvedValue([])
  render()
  await waitFor(() => expect(screen.getByText('📰 Publish')).toBeInTheDocument())
})

test('student does not see Publish button', async () => {
  api.listNews.mockResolvedValue([])
  render()
  await waitFor(() => expect(api.listNews).toHaveBeenCalled())
  expect(screen.queryByText('📰 Publish')).not.toBeInTheDocument()
})

test('publish form calls api.publishNews with title and body', async () => {
  authState.auth = { username: 'dave', role: 'Manager', isResearcher: false, token: 'tok' }
  api.listNews.mockResolvedValue([])
  render()
  await waitFor(() => screen.getByText('📰 Publish'))
  await userEvent.click(screen.getByText('📰 Publish'))
  // Modal opens — type into the title and body inputs
  await waitFor(() => document.querySelector('#news-form'))
  const form = document.querySelector('#news-form')
  const titleInput = within(form).getAllByRole('textbox')[0]
  await userEvent.type(titleInput, 'My Post')
  await userEvent.type(form.querySelector('textarea'), 'Some content here')
  fireEvent.submit(form)
  await waitFor(() =>
    expect(api.publishNews).toHaveBeenCalledWith(
      expect.objectContaining({ title: 'My Post', body: 'Some content here' })
    )
  )
})

test('search filters news by title', async () => {
  render()
  await waitFor(() => screen.getByText('Semester start'))
  await userEvent.type(screen.getByPlaceholderText('Search news…'), 'Important')
  expect(screen.queryByText('Semester start')).not.toBeInTheDocument()
  expect(screen.getByText('Important Announcement')).toBeInTheDocument()
})

test('empty state shown when no news', async () => {
  api.listNews.mockResolvedValue([])
  render()
  await waitFor(() => expect(screen.getByText('No news.')).toBeInTheDocument())
})
