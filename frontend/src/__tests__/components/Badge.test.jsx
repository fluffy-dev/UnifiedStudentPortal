import { render, screen } from '@testing-library/react'
import { Badge } from '../../components/Badge.jsx'

const cases = [
  // status tones
  { tone: 'PENDING',      cls: 'badge-yellow' },
  { tone: 'APPROVED',     cls: 'badge-green'  },
  { tone: 'REJECTED',     cls: 'badge-red'    },
  { tone: 'NOT_APPROVED', cls: 'badge-red'    },
  { tone: 'NEW',          cls: 'badge-blue'   },
  { tone: 'DONE',         cls: 'badge-green'  },
  { tone: 'PASSING',      cls: 'badge-green'  },
  { tone: 'FAILING',      cls: 'badge-red'    },
  // urgency
  { tone: 'LOW',          cls: 'badge-gray'   },
  { tone: 'MEDIUM',       cls: 'badge-yellow' },
  { tone: 'HIGH',         cls: 'badge-red'    },
  // discipline
  { tone: 'MAJOR',        cls: 'badge-blue'   },
  { tone: 'MINOR',        cls: 'badge-purple' },
  { tone: 'FREE',         cls: 'badge-gray'   },
  // grade letters
  { tone: 'A',            cls: 'badge-green'  },
  { tone: 'F',            cls: 'badge-red'    },
  // roles
  { tone: 'Admin',        cls: 'badge-red'    },
  { tone: 'Student',      cls: 'badge-blue'   },
  { tone: 'Teacher',      cls: 'badge-green'  },
  // unknown → gray fallback
  { tone: 'UNKNOWN_KEY',  cls: 'badge-gray'   },
]

test.each(cases)('tone=$tone → class $cls', ({ tone, cls }) => {
  render(<Badge tone={tone} label={tone} />)
  expect(screen.getByText(tone)).toHaveClass(cls)
})

test('renders label text from translated prop when tone provided separately', () => {
  render(<Badge tone="PENDING" label="На рассмотрении" />)
  const el = screen.getByText('На рассмотрении')
  expect(el).toHaveClass('badge-yellow')
})

test('falls back to label as tone lookup when tone is omitted', () => {
  render(<Badge label="APPROVED" />)
  expect(screen.getByText('APPROVED')).toHaveClass('badge-green')
})
