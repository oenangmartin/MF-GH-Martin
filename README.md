# Github Repository Apps
## Tech Stacks
- UI: Jetpack Compose
- Architecture: MVVM + Clean Arch (a bit)
- Concurrency Tool: Coroutine
- API Call: Retrofit
- Mock Library: Mockk
- Assertion Library: Kotest
- Image Loader: Koil
- DI/Service Locator: Koin

## App Structure
I decided to separate the app into two main module for now due to time limitation:
1. App module
   This is where all of the ui related functionality is placed. There are several packages inside the module which will be explained later
2. Network module
   This module's responsibility is to handle API call and provide the API service which will be used later in the app module, also hold the reference of network layer data

I separate the package inside app module according to it's main features, which currently we have 2 main features:
- User List (userlist inside ui package)
- User Detail (userdetail package inside ui package)
  User Detail is consist of 2 main section:
  1. User Info Section
  2. Repository List Section

## Data Flow
I use uni-directional data flow principal on this functionality
All of the state of the screen is represented by **UiState sealed interface
It's also guaranteed that I follow the immutability of the UiState (generate the new UiState everytime there's an update in data)
Also I tried to separate the network layer data with UI data to make sure that we are able to do formatting outside of the UI layer. Currently all of UI related formatting is handled by mapper classes

## Additional Notes
There are several things that still can be improved later, but due to limited amount of time, I decided not to handle them
1. The structure of the UIState is now different between the 2 main features. I realized the difference during the implementation of 2nd screen
2. Some error handling might be missing in some scenario (ie. on user detail screens where we got 2 data source)
3. Some unit test coverage might not be 100%
4. I decided to load the data in init blocks, which is not ideal, but it requires some changes in the repository layer and also some of the UiState needs to be updated
5. UI can be improved more by adding pull to refresh mechanism