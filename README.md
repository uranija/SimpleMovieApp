## SimpleMovieApp
### Project overview
The app allows users to discover the most popular movies playing. App fetches data from the Internet with **theMovieDB API**. It uses adapters and custom list layouts to populate list views.
### App features
- The app presents the user with a grid arrangement of movie posters upon launch. 
- It allows user to change sort order via a setting: The sort order can be by most popular or by highest-rated. 
- the app allows the user to tap on a movie poster and transition to a details screen with additional information such as: original title, movie poster image thumbnail, A plot synopsis (called overview in the api), user rating (called vote_average in the api), release date.
- App allows users to view and play trailers in youtube.
- App allows users to read reviews of a selected movie.
- App allows users to mark a movie as a favorite in the details view by tapping a button (star).
- App creates a database using **Room** to store the names and ids of the user's favorite movies, the rest of the information needed to display their favorites collection while offline.


### Technologies used:
App uses Android Architecture Components: **Room, LiveData, ViewModel and Lifecycle**




