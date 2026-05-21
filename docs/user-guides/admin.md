# Admin Guide

## Overview

The Admin has full system access. Admins manage user accounts, view audit logs,
and generate academic performance reports.

## User Management

### List All Users

View every account in the system with their role, faculty, and status.

**CLI:** Main menu → *List users*

**Web:** Navigate to **Administration → Users**

### Create a Student Account

Create a new student account. Required fields: username, password, first name, last name,
faculty, degree type (BACHELOR or MASTER), and study year.

**CLI:** Main menu → *Create student*

**Web:** Administration → Users → New Student

### Delete a User

Permanently remove a user account from the system.

**CLI:** Main menu → *Delete user*

**Web:** Administration → Users → select user → Delete

## Audit Log

View a timestamped log of all significant actions taken in the system (logins, enrollments,
grade changes, user creation/deletion, etc.).

**CLI:** Main menu → *View logs*

**Web:** Administration → Logs

## Academic Report

Generate a system-wide academic report showing:
- Total courses, students, and teachers
- Per-course enrollment and average score
- Top-performing students by GPA
- Count of students with failing grades

**CLI:** Main menu → *Generate academic report*

**Web:** Administration → Academic Report
