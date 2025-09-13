# Levelling 



## Web App Idea

Tech Stack: Java Sprint Boot for REST API Backend, React for Frontend, MongoDB for Database, JWT for Authentication
App: Interactive Web App with Slick Active Animations
Frontend Animations Theme: Sports Style, Energy Style
Frontend Colour Theme, Look and Vibe: RnB Type, Dark Romance, Major Colours: Red, Black, White, Highlight Colours: Canary Yellow, Light Green 
App Type: Productivity Tracker App
App name: Levelling
The App will be like a game with the following features:
The app is a self development tracker app. The app has some preset SAGAs with different Tracks. Each track has some preset arcs and every arc has some tasks associated to it... some tasks are primary (compulsory) and some are secondary (optional). Every task has certain points associated to them and every arc is completed when the compulsory tasks are complete. Completion of tasks will provide the points associated to the Arc (Arcs also have more points associated to them). 
An arc is a collection of tasks.
A track is a collection of arcs.
A saga is a collection of tracks.
A saga is collection of tracks. 
Tasks are visible when Arc is opened and marked completed with a strikethrough when done. Done tasks are also shown below the list of ongoing tasks. 
An Arc is visible when Track is opened and Marked completed with a green tick when done. Done Arcs are shown below the Ongoing Arcs. An Arc is started when Enter Arc is button is pressed on the Arc Card (also with a confirmation popup). When an arc is marked complete, the user is asked to enter Happiness Level Obtained on a scale of -10 to +10 and Effort Level Required on a scale of -10 to +10. Apart from the points, that are counted for each user, this Happiness points and the Effort Points are also counted for each day and at the end of the day, these points are converted automatically into gems according to the level/threshold they fill.. happiness for yellow gems and effort for royal blue gems. At the end of the day, 1 Gem is easy is to get.. 2 Gems also easy, 3 Gems medium but 4 is harder (higher threshold) and 5 Effort/Happiness Gems require getting a total of lot of happiness/effort points (highest threshold) at the end of the day.   
Any Track can never be complete even if all the arcs are done, its never ending. Similarly, a saga can never be complete either.

Each Saga is preset in the game.
There are preset tracks, arcs and their tracks in the game but new Track, Arc or Tasks can be created by user too.
A task can be created only in an Arc
An Arc can be created only in a Track
But a Track can be created for "Randoms Saga" by default and does not need to be in a saga. The user can add it to the saga of their choice or leave it in the default Randoms Saga.

Now, here is the real world implication to it.. The sagas are overall trackers for Important Parts of Being Someone. I would make 5 default Sagas: Career Saga, Fitness Saga, Social Saga, Hobbies Saga.
Each Saga would have their multiple tracks. I will add some pre-set tracks to all the above Sagas. But more Tracks can be created and added by the User. Each For real life implication, Each Track is a part of the saga, for example, for Career Saga, there will be Job Track, Web Dev Track, Linux Lib Dev Track, CV AI Track, LLM AI Track, and Open Source Track. Fitness Track will contain Chest Track, Arms Track, Back Track, Legs Track, Core Track, Shoulders Track, Cardio Track. Social Saga will have Partner Track, Parents Track, Relatives Track, Friends Track, Connections Track. Hobbies Saga will have Doodling Track and Football Track. Lets go with these preset tracks for now. Every Track has arcs. In real world sense, an Arc is a particular subject in that track (feild). For example, Web Dev Track would have Arcs: HTML-CSS -JS, React, NodeJS-Express, Python Flask, Java Spring Boot, Full-Stack Project. Similarly.. for all the other tracks, cook up Arcs and their tasks on your own, and so for all the other Sagas.
Also, very important... The same arc can be associated to more than one (multiple) Tracks and from different sagas and the completion of any task in that arc or that arc will be same/completed for the other track as well (it is the same arc associated, not a copy of it).
Apart from this regular Saga System, there is also a Daily Objectives Section. Users will choose their daily objective tasks themselves (Tasks can be chosen from any arc in the saga system) and upon completing them, a user will get Regularity Points according to the tasks completed. At the end of the week (Sunday 23:59), the regularity points will get converted to Crimson Red Gems. 
Apart from the sagas and the daily objectives, there are also weekly objectives which are Arcs chosen by User, Completing these weekly objectives provide Skill Points which are converted to Emerald Green Gems at the end of the Month.
These were all the ways to earn points and different gems.
Now, There is a Marketplace. Marketplace consists of Items. An Item will have 5 compulsory properties: an Image, a name, a price (user will enter real world price -> convert it to game currency automatically on your own with a simple algo), a tag (depicting what type of item it is... clothing, jewellery, computer accessory, etc.) and impact (depicting what the item brings to the table like.. Fashion, Utility, etc.) There are also 2 optional properties: Store Link of the Item and Description.
There will also be a section of Owned Items which will list all the items bought from marketplace.
Apart from this, there is also a History Section, which displays all the tasks/arcs/tracks/achievements completed in reverse chronological order. The user must also have an inbox on which the notifications of Achievements, Daily Objective Points, Weekly Objective Points earned, Info about recent Gems Conversions must be sent. The inbox should open up like a modal and should highlight the unread messages.
The items in the marketplace can be bought using the points and gems and the bought items will go in the section of items owned.
This is the basic structure of the App.
Create the backend API for all the required features using all these information.

Also, Here is a description of the frontend...
The entire app should have scrolling animations with parallax effect on the background
The home page appears with a hero element with login/signup options. When Login is started,  that appears with a horizontal parallax on the hero element, user can enter their username and password and save their info session on the browser. Then, whenever the user opens the app in the browser, the user sees the hero element with Enter Button. On pressed, the user enters the Sagas Page with a "Entering the game animation" transition. There should be navbar of Sagas, Objectives, Marketplace. The Sagas Appear as Big Cards (with an image, name and effect). On clicking each card, we see their tracks with entering the game animation transition. Now, we shall have a Back button at the tracks page. Each Track Appears in the Sidebar with an image in background and name. The main page of the track being a big image with description and header of the Track. On clicking the track header in the sidebar, that track's page will appear while the sidebar remains in the same place. On the track content area, The Ongoing Arcs appear as big cards, clicking which a modal appears with all the tasks and their points listed in the arc card horizontally stacked and a Create task option. The Ongoing Arcs are stacked horizontally and can be scrolled in the enclosed area of Content Area without getting the actual page scrolled. There are also the completed arcs listed below the Ongoing Arcs in rectangular card (which has a low height but occupies the entire width of the content area now stacked vertically). There is also a create Arc option that opens with horizontal parallax and provides the form to create a new Arc. When image for arc is not provided, a placeholder image is used.
The Objectives Section would have its own sidebar, with daily and weekly objectives as headers. Also, a button of Create Objective should be there, clicking which, should open up the Add Objective page with horizontal parallax. The objective cards should be stacked vertically (card occupies content area width) 
The marketplace should contain cards with content area width, with all the properties listed column-wise and all the cards stacked vertically. Each item should have a buy button. While buying an item, there should be confirmational pop-up modal before buying it. 
In the Owned Items section, along with all the item properties, there must be the real world price shown too.
The app when user is entered in the session, must always show the Gems, Coins Earned and separate Meters of Happiness, Regularity, Skill Points in the top right hand side. The inbox should also be placed there alongside the meters.

Also, a slight modification. Instead of making the Sage Page as the first app page after Hero / Login, make the History Page as the first Page after Hero / Login. Also, make it look like a timeline bar where the current day is highlighted in the middle of the page and its circle is bigger than the previous day's circles on the timeline bar. Scrolling down would scroll down to the previous days and respectively, the active day (current day on scroll)'s circle would be big. Design the History Page like this... like a vertical timeline bar. Inside the circle, there is the date written. Month, Year in the Left Top of content container. And the events of the active day appearing in the left. Only The events of the active day are shown. So, events of the previous days will be shown only when scrolled down to those respective days.

## Backend API Routes

The Backend is a robust and comprehensive Spring Boot API that securely handles everything from user authentication to the core gameplay loops, automated gem conversions, and the marketplace. The architecture is designed to correctly separate public preset content from private user-created content.

---

### **Complete API Route Guide for "Levelling"**

**Base URL:** All routes are prefixed with `http://localhost:8080`

#### **1. Authentication (`/api/v1/auth`)**

These endpoints are for user registration and login.

| Method | URL Path               | Auth     | Purpose                                   | Request Body (JSON)                                            | Success Response (2xx)                                 |
| :----- | :--------------------- | :------- | :---------------------------------------- | :------------------------------------------------------------- | :----------------------------------------------------- |
| `POST` | `/api/v1/auth/register`  | **Public** | Creates a new user account.               | `{"username": "...", "email": "...", "password": "..."}`        | `201 CREATED`: "User registered successfully!"         |
| `POST` | `/api/v1/auth/login`     | **Public** | Authenticates a user and gets a JWT token. | `{"usernameOrEmail": "...", "password": "..."}`                  | `200 OK`: `{"accessToken": "...", "tokenType": "Bearer"}` |

---

#### **2. User (`/api/v1/users`)**

These endpoints relate to the logged-in user's data and inventory.

| Method | URL Path                  | Auth             | Purpose                                          | Request Body | Success Response (2xx)                                            |
| :----- | :------------------------ | :--------------- | :----------------------------------------------- | :----------- | :---------------------------------------------------------------- |
| `GET`  | `/api/v1/users/me`          | **Bearer Token** | Gets the profile of the currently logged-in user.  | *None*       | `200 OK`: The full `User` object (including currencies).            |
| `GET`  | `/api/v1/users/me/inventory`| **Bearer Token** | Gets the list of items the user owns.              | *None*       | `200 OK`: A list of `OwnedItemDto` objects.                       |
| `GET`  | `/api/v1/users/me/history`  | **Bearer Token** | Gets the user's chronological history of completions. | *None*       | `200 OK`: A list of `HistoryEventDto` objects, sorted newest first. |

---

#### **3. Sagas (`/api/v1/sagas`)**

This endpoint retrieves the top-level Sagas.

| Method | URL Path          | Auth             | Purpose                       | Request Body | Success Response (2xx)                        |
| :----- | :---------------- | :--------------- | :---------------------------- | :----------- | :-------------------------------------------- |
| `GET`  | `/api/v1/sagas`   | **Bearer Token** | Retrieves all available Sagas. | *None*       | `200 OK`: A list of `Saga` objects.             |

---

#### **4. Content (`/api/v1/content`)**

These endpoints are used to drill down into the game's content.

| Method | URL Path               | Auth             | Purpose                                           | Request (Query Params) | Success Response (2xx)                            |
| :----- | :--------------------- | :--------------- | :------------------------------------------------ | :--------------------- | :------------------------------------------------ |
| `GET`  | `/api/v1/content/tracks` | **Bearer Token** | Gets tracks for a specific Saga.                    | `?sagaId=<saga_id>`      | `200 OK`: A list of relevant `Track` objects.       |
| `GET`  | `/api/v1/content/arcs`   | **Bearer Token** | Gets arcs for a specific Track.                     | `?trackId=<track_id>`    | `200 OK`: A list of relevant `Arc` objects.         |
| `GET`  | `/api/v1/content/tasks`  | **Bearer Token** | Gets tasks for a specific Arc.                      | `?arcId=<arc_id>`        | `200 OK`: A list of relevant `Task` objects.        |

---

#### **5. User Progress (`/api/v1/progress`)**

These endpoints are for the user to mark items as complete.

| Method | URL Path                               | Auth             | Purpose                                        | Request Body (JSON)                                | Success Response (2xx)                                |
| :----- | :------------------------------------- | :--------------- | :--------------------------------------------- | :------------------------------------------------- | :---------------------------------------------------- |
| `POST` | `/api/v1/progress/tasks/{taskId}/complete` | **Bearer Token** | Marks a single task as complete.                 | *None*                                             | `200 OK`: "Task marked as complete."                  |
| `POST` | `/api/v1/progress/arcs/{arcId}/complete`   | **Bearer Token** | Marks a single arc as complete (if tasks are done). | `{"happinessLevel": number, "effortLevel": number}` | `200 OK`: "Arc marked as complete."                   |

---

#### **6. Objectives (`/api/v1/objectives`)**

These endpoints allow the user to set their daily and weekly goals.

| Method | URL Path                  | Auth             | Purpose                                | Request Body (JSON)         | Success Response (2xx)                               |
| :----- | :------------------------ | :--------------- | :------------------------------------- | :-------------------------- | :--------------------------------------------------- |
| `POST` | `/api/v1/objectives/daily`  | **Bearer Token** | Sets or updates the daily objectives.  | `{"ids": ["<taskId1>", ...]}` | `200 OK`: "Daily objectives have been set."          |
| `POST` | `/api/v1/objectives/weekly` | **Bearer Token** | Sets or updates the weekly objectives. | `{"ids": ["<arcId1>", ...]}`  | `200 OK`: "Weekly objectives have been set."         |

---

#### **7. Marketplace (`/api/v1/marketplace`)**

These endpoints handle viewing and purchasing items.

| Method | URL Path                           | Auth             | Purpose                               | Request Body | Success Response (2xx)                              |
| :----- | :--------------------------------- | :--------------- | :------------------------------------ | :----------- | :-------------------------------------------------- |
| `GET`  | `/api/v1/marketplace/items`        | **Bearer Token** | Retrieves all items for sale.         | *None*       | `200 OK`: A list of `Item` objects.                   |
| `POST` | `/api/v1/marketplace/items/{itemId}/buy` | **Bearer Token** | Purchases a single item.                | *None*       | `200 OK`: "Item purchased successfully."            |

---

#### **8. Notifications (`/api/v1/notifications`)**

These endpoints manage the user's inbox.

| Method | URL Path                    | Auth             | Purpose                                          | Request Body | Success Response (2xx)                              |
| :----- | :-------------------------- | :--------------- | :----------------------------------------------- | :----------- | :-------------------------------------------------- |
| `GET`  | `/api/v1/notifications`       | **Bearer Token** | Retrieves all notifications for the user, newest first. | *None*       | `200 OK`: A list of `Notification` objects.         |
| `POST` | `/api/v1/notifications/read-all` | **Bearer Token** | Marks all of the user's notifications as read.   | *None*       | `200 OK`: "All notifications marked as read."       |



