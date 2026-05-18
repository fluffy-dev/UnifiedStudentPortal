import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { I18nProvider, useI18n } from '../../context/I18nContext.jsx'

const BUNDLE_EN = {
  'ui.welcome_back': 'Welcome back',
  'ui.0_courses_available': '{0} courses available',
  'PENDING': 'Pending',
}
const BUNDLE_RU = {
  'ui.welcome_back': 'С возвращением',
  'ui.0_courses_available': 'Доступно курсов: {0}',
  'PENDING': 'На рассмотрении',
}

function makeFetch(lang) {
  return Promise.resolve({
    ok: true,
    json: () => Promise.resolve(lang === 'ru' ? BUNDLE_RU : BUNDLE_EN),
  })
}

function Probe() {
  const { t, language, changeLanguage } = useI18n()
  return (
    <div>
      <span data-testid="lang">{language}</span>
      <span data-testid="t1">{t('ui.welcome_back')}</span>
      <span data-testid="t2">{t('ui.0_courses_available', 5)}</span>
      <span data-testid="t3">{t('no.such.key')}</span>
      <span data-testid="t4">{t('PENDING')}</span>
      <button onClick={() => changeLanguage('ru')}>ru</button>
    </div>
  )
}

function setup() {
  render(<I18nProvider><Probe /></I18nProvider>)
  return waitFor(() =>
    expect(screen.getByTestId('t1')).not.toHaveTextContent('Loading')
  )
}

beforeEach(() => {
  vi.spyOn(global, 'fetch').mockImplementation((_, opts) => {
    const lang = opts?.headers?.['Accept-Language'] ?? 'en'
    return makeFetch(lang)
  })
})
afterEach(() => vi.restoreAllMocks())

test('loads EN bundle and translates keys', async () => {
  await setup()
  expect(screen.getByTestId('t1')).toHaveTextContent('Welcome back')
  expect(screen.getByTestId('t4')).toHaveTextContent('Pending')
})

test('interpolates {0} placeholders', async () => {
  await setup()
  expect(screen.getByTestId('t2')).toHaveTextContent('5 courses available')
})

test('falls back to key string when not found', async () => {
  await setup()
  expect(screen.getByTestId('t3')).toHaveTextContent('no.such.key')
})

test('changeLanguage switches bundle to RU and persists to localStorage', async () => {
  await setup()
  await userEvent.click(screen.getByText('ru'))
  await waitFor(() =>
    expect(screen.getByTestId('t1')).toHaveTextContent('С возвращением')
  )
  expect(screen.getByTestId('t4')).toHaveTextContent('На рассмотрении')
  expect(localStorage.getItem('lang')).toBe('ru')
})

test('purges stale i18n_ keys from localStorage on mount', async () => {
  localStorage.setItem('i18n_v2_en', JSON.stringify({ ts: 0, data: {} }))
  localStorage.setItem('i18n_old_cache', 'stale')
  await setup()
  // jsdom localStorage uses Object.keys correctly, so purge should work
  expect(localStorage.getItem('i18n_v2_en')).toBeNull()
  expect(localStorage.getItem('i18n_old_cache')).toBeNull()
})

test('fetch is called with correct Accept-Language header', async () => {
  await setup()
  expect(fetch).toHaveBeenCalledWith(
    expect.stringContaining('/api/system/messages'),
    expect.objectContaining({
      headers: expect.objectContaining({ 'Accept-Language': 'en' }),
    })
  )
})
