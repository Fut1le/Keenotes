# Keenotes
**Android Social App &amp; News**

[Website](https://vl-keenotes.web.app/) | [Google Play](https://play.google.com/store/apps/details?id=com.ruya.takimi.keenotes)

### Keenotes is a social network focused on the privacy and data protection of our users. Beautiful design, fast and stable performance, many functions will give you the best experience. Share photos with your friends, ask your questions on the forum, communicate with your friends using messages, be aware of all the news from Keenotes!

![banner](https://user-images.githubusercontent.com/43324348/160253567-0b58b516-a704-4413-8795-a889518d61b3.jpg)


<!------------------------------------------------------------------------->

This is my school project. Based on it, I studied the structure of social networks and tried to do something of my own based on Google Firebase. At this stage, the application is at the completion stage. If you like my project and want to help with development, please contact: mr.vovanumberone@gmail.com.


This application also shows the work of my modules well, you can find them here:
[Music-streaming](https://github.com/Fut1le/Firebase-Music-Streaming) & [Video-streaming](https://github.com/Fut1le/Firebase-Video-Streaming)

<!------------------------------------------------------------------------->


## Features
- Share photos and thoughts with your friends
- Read the news
- Find new friends and chat with them
- Share documents and location
- Comments & Likes
- Listen the music
- Watch the video
- Upload your favorite music or video
- Explore the forum
- Play games
- Light and dark theme
- Smooth fluid design
- No ads

## Screenshots

<table>
  <thead>
    <tr>
      <th>Light Mode</th>
      <th>Dark Mode</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <img
          src="https://github.com/Fut1le/Keenotes/blob/master/screenshots/dashboard_light.jpg"
        />
      </td>
      <td>
        <img
          src="https://github.com/Fut1le/Keenotes/blob/master/screenshots/dashboard_dark.jpg"
        />
      </td>
    </tr>
    <tr>
      <td>
        <img
          src="https://github.com/Fut1le/Keenotes/blob/master/screenshots/music_light.jpg"
        />
      </td>
      <td>
        <img
          src="https://github.com/Fut1le/Keenotes/blob/master/screenshots/music_dark.jpg"
        />
      </td>
    </tr>
    <tr>
      <td>
        <img
          src="https://github.com/Fut1le/Keenotes/blob/master/screenshots/profile_light.jpg"
        />
      </td>
      <td>
        <img
          src="https://github.com/Fut1le/Keenotes/blob/master/screenshots/profile_dark.jpg"
        />
      </td>
    </tr>
    <tr>
      <td>
        <img
          src="https://github.com/Fut1le/Keenotes/blob/master/screenshots/video_light.jpg"
        />
      </td>
      <td>
        <img
          src="https://github.com/Fut1le/Keenotes/blob/master/screenshots/video_dark.jpg"
        />
      </td>
    </tr>
  </tbody>
</table>



<!------------------------------------------------------------------------->

## How to setup my app
1. Clone this repo
2. Open in Adroid Studio
3. Create new Firebase project
4. Rename the project package name
5. Add **google-services.json** in /app folder
6. In the Firebase console switch on:
   - Auth
   - Firestore
   - Storage
   - Real-time database
   - Hosting
7. Edit the Firebase Storage rules
8. Have fun! 😏


# Cloud Firestore Rules:
```
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```
# Storage Rules:
```
service firebase.storage {
    match /b/YOUR_APP_ID.appspot.com/o {
        match /{allPaths=**} {
            allow read, write: if true;
        }
    }
}
```
# Realtime Database Rules:
```
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```
<!------------------------------------------------------------------------->

Copyright ©2022 All rights reserved.
