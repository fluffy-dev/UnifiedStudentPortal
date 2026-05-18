import { render } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'

export const BUNDLE = {
  'ui.welcome_back': 'Welcome back', 'ui.you_are_logged_in_as': 'You are logged in as',
  'ui.courses_available': 'Courses available', 'ui.inbox_messages': 'Inbox messages',
  'ui.latest_news': 'Latest news', 'ui.latest_news_1': 'Latest News',
  'ui.pinned': 'PINNED', 'ui.pinned_1': 'Pinned',
  'ui.recent_messages': 'Recent Messages', 'ui.from_1': 'from',
  'ui.courses_1': 'Courses', 'ui.0_courses_available': '{0} courses available',
  'ui.new_course': 'New Course', 'ui.search_courses': 'Search courses…',
  'ui.id': 'ID', 'ui.credits': 'credits', 'ui.seats': 'seats',
  'ui.enroll': 'Enroll', 'ui.drop': 'Drop', 'ui.full': 'FULL',
  'ui.course_name': 'Course Name', 'ui.credits_1': 'Credits', 'ui.capacity': 'Capacity',
  'ui.type': 'Type', 'ui.create': 'Create',
  'course.list.empty': 'No courses available.', 'common.back': 'Back',
  'manager.menu.create_course': 'Create Course',
  'ui.help_requests': 'Help Requests', 'ui.0_requests': '{0} requests',
  'ui.new_request': 'New Request', 'ui.search_requests': 'Search requests…',
  'ui.title': 'Title', 'ui.description': 'Description', 'ui.urgency': 'Urgency',
  'ui.submit': 'Submit', 'ui.submit_help_request': 'Submit Help Request',
  'ui.no_requests': 'No requests', 'ui.status': 'Status', 'ui.created': 'Created',
  'ui.read_more': 'Read more', 'ui.collapse': 'Collapse', 'ui.by': 'by',
  'ui.approve': 'Approve', 'ui.reject': 'Reject', 'ui.all': 'All',
  'PENDING': 'Pending', 'APPROVED': 'Approved', 'REJECTED': 'Rejected',
  'TRANSCRIPT_FOR_SEMESTER': 'Transcript for semester',
  'ACADEMIC_MOBILITY': 'Academic mobility',
  'CERTIFICATE_OF_EDUCATION': 'Certificate of education',
  'LOW': 'Low', 'MEDIUM': 'Medium', 'HIGH': 'High',
  'ui.messages': 'Messages', 'ui.0_in_inbox': '{0} in inbox', 'ui.compose': 'Compose',
  'ui.subject': 'Subject', 'ui.from': 'From', 'ui.sent': 'Sent', 'ui.send': 'Send',
  'ui.body': 'Body', 'ui.to_username': 'To (username)', 'ui.recipient': 'Recipient',
  'inbox.empty': 'Your inbox is empty.', 'ui.message_sent': 'Message sent!',
  'ui.search_messages': 'Search messages…',
  'student.menu.news': 'News', 'ui.publish': 'Publish',
  'ui.latest': 'Latest', 'news.empty': 'No news.', 'ui.comment': 'Comment',
  'ui.comments': 'comments', 'ui.post_1': 'Post',
  'ui.write_your_comment': 'Write your comment…', 'ui.add_comment': 'Add Comment',
  'ui.publish_news': 'Publish News', 'ui.pin_this_post': 'Pin this post',
  'ui.news_published': 'News published!', 'ui.comment_posted': 'Comment posted!',
  'ui.search_news': 'Search news…',
  'teacher.menu.put_marks': 'Put Marks',
  'ui.select_a_course_to_view_or_enter_grades': 'Select a course to view or enter grades',
  'ui.select_a_course': 'Select a course', 'ui.student_grades': 'Student Grades',
  'ui.record_marks': 'Record Marks', 'ui.student': 'Student',
  'ui.1st': '1st', 'ui.2nd': '2nd', 'ui.exam': 'Exam', 'ui.total': 'Total',
  'ui.grade': 'Grade', 'ui.passing': 'Passing', 'ui.no_grades_yet': 'No grades yet',
  'ui.save': 'Save', 'ui.1st_attestation_0_30': '1st Att (0-30)',
  'ui.2nd_attestation_0_30': '2nd Att (0-30)', 'ui.exam_0_40': 'Exam (0-40)',
  'ui.no_courses_assigned': 'No courses assigned',
  'ui.marks_recorded': 'Marks recorded!', 'ui.not_admitted_hint': 'Not admitted to exam',
  'PASSING': 'Passing', 'FAILING': 'Failing',
  'ui.it_orders': 'IT Orders', 'ui.new_order': 'New Order',
  'ui.0_orders_in_queue': '{0} orders in queue', 'ui.device_type': 'Device Type',
  'ui.order_created': 'Order created!', 'ui.no_orders': 'No orders',
  'ui.accept': 'Accept', 'ui.complete': 'Complete', 'ui.actions': 'Actions',
  'ui.user': 'User', 'ui.device': 'Device', 'ui.search_orders': 'Search orders…',
  'ui.create_it_order': 'Create IT Order',
  'NEW': 'New', 'DONE': 'Done', 'ACCEPTED': 'Accepted',
  'admin.menu.users': 'Users', 'ui.0_total_users': '{0} total users',
  'ui.create_student': 'Create Student', 'ui.search_users': 'Search users…',
  'ui.username': 'Username', 'ui.full_name': 'Full Name', 'ui.email': 'Email',
  'ui.faculty': 'Faculty', 'ui.role': 'Role', 'ui.password': 'Password',
  'ui.first_name': 'First Name', 'ui.last_name': 'Last Name',
  'ui.year': 'Year', 'ui.degree': 'Degree',
  'admin.menu.delete_user': 'Delete', 'ui.student_created': 'Student created!',
  'ui.user_deleted': 'User deleted',
  'BACHELOR': 'Bachelor', 'MASTER': 'Master', 'DOCTORATE': 'Doctorate',
  'Student': 'Student', 'Teacher': 'Teacher', 'Manager': 'Manager',
  'Admin': 'Admin', 'Librarian': 'Librarian', 'ui.students': 'Students',
  'ui.university_system': 'University System',
  'ui.sign_in_to_continue': 'Sign in to continue',
  'ui.sign_in': 'Sign in', 'ui.signing_in': 'Signing in…',
  'ui.demo_admin_admin_u00a0_u00b7_u00a0_eve_e': 'Demo accounts',
  'Invalid credentials.': 'Invalid credentials.',
}

export function makeT(bundle) {
  return (key, ...args) => {
    let msg = bundle[key] ?? key
    args.forEach((arg, i) => { msg = msg.replace(`{${i}}`, arg) })
    return msg
  }
}

export const t = makeT(BUNDLE)

export function renderWith(ui, { route = '/' } = {}) {
  return render(<MemoryRouter initialEntries={[route]}>{ui}</MemoryRouter>)
}
