import { beforeEach, describe, expect, test, vi } from 'vitest'

vi.mock('../../api/client.js', () => ({
  get:  vi.fn(),
  post: vi.fn(),
  put:  vi.fn(),
  del:  vi.fn(),
}))
import * as client from '../../api/client.js'
import * as api from '../../api/index.js'

beforeEach(() => vi.clearAllMocks())

describe('auth', () => {
  test('login sends username and password via POST /login', () => {
    api.login('alice', 'pass')
    expect(client.post).toHaveBeenCalledWith('/login', { username: 'alice', password: 'pass' })
  })
  test('logout calls POST /logout', () => {
    api.logout()
    expect(client.post).toHaveBeenCalledWith('/logout')
  })
})

describe('users', () => {
  test('userDirectory calls GET /users/directory', () => {
    api.userDirectory()
    expect(client.get).toHaveBeenCalledWith('/users/directory')
  })
  test('listUsers calls GET /users', () => {
    api.listUsers()
    expect(client.get).toHaveBeenCalledWith('/users')
  })
  test('createStudent posts to /users/students', () => {
    const data = { username: 'x', password: 'y' }
    api.createStudent(data)
    expect(client.post).toHaveBeenCalledWith('/users/students', data)
  })
  test('deleteUser calls DELETE /users/{username}', () => {
    api.deleteUser('alice')
    expect(client.del).toHaveBeenCalledWith('/users/alice')
  })
})

describe('courses', () => {
  test('listCourses calls GET /courses', () => {
    api.listCourses()
    expect(client.get).toHaveBeenCalledWith('/courses')
  })
  test('enroll calls POST /courses/{id}/enroll', () => {
    api.enroll('CRS-1')
    expect(client.post).toHaveBeenCalledWith('/courses/CRS-1/enroll')
  })
  test('drop calls POST /courses/{id}/drop', () => {
    api.drop('CRS-2')
    expect(client.post).toHaveBeenCalledWith('/courses/CRS-2/drop')
  })
  test('recordMarks posts to /courses/{id}/marks', () => {
    const body = { studentUsername: 'eve', firstHalf: 25, secondHalf: 28, exam: 35 }
    api.recordMarks('CRS-1', body)
    expect(client.post).toHaveBeenCalledWith('/courses/CRS-1/marks', body)
  })
  test('viewGrades calls GET /courses/{id}/grades', () => {
    api.viewGrades('CRS-1')
    expect(client.get).toHaveBeenCalledWith('/courses/CRS-1/grades')
  })
})

describe('requests', () => {
  test('submitRequest posts correct body', () => {
    const body = { title: 'x', type: 'TRANSCRIPT_FOR_SEMESTER', urgency: 'HIGH', body: 'y' }
    api.submitRequest(body)
    expect(client.post).toHaveBeenCalledWith('/requests', body)
  })
  test('processRequest puts { status } to /requests/{id}', () => {
    api.processRequest(3, 'APPROVED')
    expect(client.put).toHaveBeenCalledWith('/requests/3', { status: 'APPROVED' })
  })
})

describe('orders', () => {
  test('createOrder posts description and deviceType', () => {
    api.createOrder({ description: 'Keyboard', deviceType: 'LAPTOP' })
    expect(client.post).toHaveBeenCalledWith('/orders', { description: 'Keyboard', deviceType: 'LAPTOP' })
  })
  test('acceptOrder puts to /orders/{id}/accept', () => {
    api.acceptOrder(5)
    expect(client.put).toHaveBeenCalledWith('/orders/5/accept')
  })
  test('completeOrder puts to /orders/{id}/complete', () => {
    api.completeOrder(5)
    expect(client.put).toHaveBeenCalledWith('/orders/5/complete')
  })
})

describe('messages', () => {
  test('inbox calls GET /messages/inbox', () => {
    api.inbox()
    expect(client.get).toHaveBeenCalledWith('/messages/inbox')
  })
  test('sendMessage posts recipient/subject/body/urgency', () => {
    const msg = { recipient: 'bob', subject: 'Hi', body: 'Hey', urgency: 'LOW' }
    api.sendMessage(msg)
    expect(client.post).toHaveBeenCalledWith('/messages', msg)
  })
})

describe('news', () => {
  test('listNews calls GET /news', () => {
    api.listNews()
    expect(client.get).toHaveBeenCalledWith('/news')
  })
  test('publishNews posts title/body/pinned', () => {
    api.publishNews({ title: 'T', body: 'B', pinned: true })
    expect(client.post).toHaveBeenCalledWith('/news', { title: 'T', body: 'B', pinned: true })
  })
  test('commentOnNews posts { comment } to /news/{id}/comment', () => {
    api.commentOnNews(7, 'Great!')
    expect(client.post).toHaveBeenCalledWith('/news/7/comment', { comment: 'Great!' })
  })
})

describe('research', () => {
  test('becomeResearcher posts { field }', () => {
    api.becomeResearcher('AI')
    expect(client.post).toHaveBeenCalledWith('/research/become', { field: 'AI' })
  })
  test('publishPaper posts title/journal/pages/doi', () => {
    const p = { title: 'T', journal: 'J', pages: 10, doi: '10.x' }
    api.publishPaper(p)
    expect(client.post).toHaveBeenCalledWith('/papers', p)
  })
  test('createProject posts journal and topic', () => {
    api.createProject({ journal: 'AI', topic: 'ML' })
    expect(client.post).toHaveBeenCalledWith('/projects', { journal: 'AI', topic: 'ML' })
  })
  test('subscribe posts { journal }', () => {
    api.subscribe('Nature')
    expect(client.post).toHaveBeenCalledWith('/subscriptions', { journal: 'Nature' })
  })
  test('unsubscribe deletes /subscriptions/{journal} (URL-encoded)', () => {
    api.unsubscribe('AI Journal')
    expect(client.del).toHaveBeenCalledWith('/subscriptions/AI%20Journal')
  })
})
