import '@testing-library/jest-dom'

// jsdom provides a real localStorage — just clear it before every test
// so tests never bleed state into each other
beforeEach(() => {
  localStorage.clear()
  vi.clearAllMocks()
})
