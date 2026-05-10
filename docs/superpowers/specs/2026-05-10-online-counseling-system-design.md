# Online Counseling System Design

## Goal

Build a complete student-graduation-project online psychological counseling system using Java, Spring Boot, MyBatis, MySQL, Vue, Axios, and Element Plus. The system must cover all requirements extracted from the draft document while keeping the business logic simple enough to implement and demonstrate reliably.

## Confirmed Scope

The project will use the strict MySQL deployment approach requested by the user. Runtime data is stored in MySQL, with SQL scripts for schema creation and test data. Because the current machine does not have MySQL, Maven, or Gradle installed, the repository will include:

- Maven Wrapper for backend builds.
- Docker Compose for local MySQL 8 startup.
- SQL schema and seed data scripts.
- Clear README instructions for running with MySQL.

Online consultation will be implemented as consultation messages and teacher replies instead of complex real-time WebSocket chat. This preserves the consultation requirement while keeping the system stable and appropriate for a student project.

## Architecture

The repository is a monorepo:

- `backend`: Spring Boot REST API, MyBatis persistence, MySQL storage, integration tests.
- `frontend`: Vue single-page application, Element Plus UI, Axios API client, role-based pages.
- `database`: MySQL schema and seed data.
- `docs`: project documentation.

The backend exposes JSON REST endpoints. The frontend stores the logged-in user in local storage and shows menus based on the selected role. This is intentionally simple and transparent for a student project.

## Roles

### Student User

Student users can register, log in, view announcements, browse counselor services, take psychological tests, submit appointments, submit consultation messages, and view teacher replies.

### Psychological Teacher

Teachers can register, log in after approval, manage appointment reviews, view consultations, reply to consultations, maintain psychological test papers and questions, and view test records.

### Administrator

Administrators can log in, manage users, approve teachers, manage announcements, manage tests, view appointments, view consultations, maintain system information, and manage basic configuration.

## Functional Modules

### Authentication

- Register student users.
- Register teacher accounts.
- Login as student, teacher, or administrator.
- Return user identity and role-specific access information.
- Reject login for unapproved teachers.

### Announcement Management

- Public announcement list and detail.
- Administrator create, update, delete, and list announcements.

### Teacher and Service Management

- Student users browse teacher service cards.
- Teachers maintain their basic profile through seeded and editable data.
- Administrators review teacher approval status.

### Psychological Test

- Students view active tests.
- Students answer single-choice questions.
- Backend calculates total score.
- Backend derives a simple result level from score.
- Test records are stored and queryable.
- Teachers and administrators can maintain test papers and questions.

### Appointment

- Students submit appointments for teachers.
- Initial appointment status is `PENDING`.
- Teachers approve or reject appointments and write review comments.
- Students, teachers, and administrators can view appointment records relevant to their role.

### Consultation and Reply

- Students submit consultation messages to teachers.
- Teachers view consultation messages and create replies.
- Students view replies.
- Administrators can view consultation records for platform management.

### User and System Management

- Administrators manage student account status.
- Administrators approve or reject teacher accounts.
- Administrators maintain about-us/system information and basic configuration.

## Data Model

The MySQL database will contain these main tables:

- `users`: student users.
- `teachers`: psychological teachers.
- `admins`: administrators.
- `announcements`: platform announcements.
- `psychological_tests`: test papers.
- `test_questions`: single-choice questions.
- `test_records`: student answers and scores.
- `services`: teacher service information.
- `appointments`: consultation appointments.
- `consultations`: student consultation messages.
- `consultation_replies`: teacher replies.
- `system_infos`: about-us and platform information.
- `system_configs`: key-value system configuration.

## UI Design

The frontend will be a polished operational application rather than a marketing site. It will use:

- Clean dashboard layout with fixed sidebar and top bar.
- Soft clinical palette using white, teal, blue, and restrained violet accents.
- Compact professional cards with 8px radius.
- Role-aware dashboard summaries.
- Tables for management workflows.
- Forms and dialogs for create/edit/review actions.
- Responsive layout for desktop and tablet-width screens.

The first screen is the usable login/register experience, not a landing page.

## Error Handling

Backend endpoints return consistent JSON responses with `success`, `message`, and `data`. Validation errors return readable Chinese messages. Frontend pages show Element Plus notifications for success and failure.

## Testing

Backend integration tests will cover:

- Login for admin, teacher, and student.
- Student registration.
- Announcement list and admin creation.
- Psychological test submit and score record creation.
- Appointment submission and teacher review.
- Consultation submission and teacher reply.
- Admin teacher approval and user status management.

Frontend manual/browser testing will cover:

- Login as each role.
- Student test, appointment, consultation, and reply viewing.
- Teacher appointment review, consultation reply, and test management.
- Admin announcement, user, teacher, test, appointment, consultation, and system pages.

## GitHub Delivery

The repository will include:

- Source code.
- SQL scripts.
- Docker Compose for MySQL.
- README with startup steps, test accounts, features, screenshots/testing notes, and project structure.
- Git history with meaningful commits.

After implementation and verification, the repository will be created on GitHub using the logged-in GitHub CLI account and pushed.
