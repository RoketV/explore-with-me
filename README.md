# Find-event
"Find-event" application is a poster that you can use to share upcoming events and find company to spend time with. It is a multi-module app with two microservices.

**The app is divided into two services:**

1. The main service - contains everything you need for the product. It is divided into three parts:

- Public - available without registration to any user of the network
- Private - available only to the authorized users
- Administrative - for the service administrators

The public API provides searching and filtering capabilities:

- Sorting of the events list is organized either by the number of hits, which will be requested in the statistics service, or by dates of events
- Only brief information on events is returned when viewing the list of events
- Each public request for a list of events or complete information about the event
is recorded by the statistics service
 
The closed part of API realizes the possibilities of the registered users of the product:

- Authorized users have an opportunity to add new events to the application, edit them and view them after addition
- Opportunity to apply for participation in events of interest
- The event creator has an opportunity to confirm applications, which were sent by other users of the service

The administrative part of the API provides options to configure and support the service:

- The administrator can add, change and delete categories for events
- The administrator can add, delete and fix a selection of events on the main page
- Moderation of events posted by users - publication or rejection
- User management - adding, activating, viewing and deleting

2. Statistics service - stores the number of views and allows you to make various selections to analyze the application's performance

The functionality of the statistics service contains:

- Recording information that a request to API endpoints (GET /events, GET /events/{id}) was processed
- Providing statistics for selected dates on selected endpoints

**Additional function - comments to events.**

It is divided into three parts:

- Public - available without registration to any user of the network
- Private - available only to the authorized users
- Administrative - for service administrators

Public - opportunity to view the published comments:

- Sorting of comments is organized by date of creation or by id by default

Closed - the abilities of the registered users of the product are implemented:

- Adding a comment to the published event
- Editing or deleting the comment if it has not been moderated yet
- Getting the comment by id

Administrative - moderation of the content of comments:

- Moderation of comments posted by users - publication or rejection

**The life cycle of an event includes several stages:**
- Creation
- Waiting for publication. The event enters the pending publication status as soon as it is created
- Publication. An administrator puts the event into this state
- Cancel publication. Event goes to this state in two cases. First, if the administrator has decided that it should not be published. The second is when the event initiator decided to cancel it on the stage of waiting for publication

The main service and the statistics service save and download data from different databases. The interaction of services at the moment of saving information about a request to API endpoints is carried out with the help of the statistical client

**DB diagram of the main service**

![db schema](https://github.com/RoketV/find-event/assets/104717438/844b1d49-ee8a-45de-bead-d9624642b107)

**DB diagram of the statistics**

![db schema stat](https://github.com/RoketV/find-event/assets/104717438/72870acb-4dea-4d4c-b1cf-17fe999aa2d3)

