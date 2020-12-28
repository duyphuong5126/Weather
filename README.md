## Table of content
* [Done items](done-items)
* [Architecture & Techniques](architecture-&-techniques)
* [Project structure](project-structure)
* [Main libraries](main-libraries)
* [Setup project](setup-project)
* [Requisites](requisites)

## Done items
Features:
* The application can retrieve the weather information from the OpenWeatherMaps API.
* The application allows users to input the searching term.
* The application can proceed searching with a condition of the search term length must be from 3 characters or above.
* The application can render the searched results as a list of weather items.
* The application supports the caching mechanism to prevent the app from generating a bunch of API requests.
* The application can manage the caching mechanism & lifecycle.
* The application can handle failures.

Technical requirements:
* Kotlin (100%)
* App's architecture: MVVM
* Apply LiveData mechanism
* UI looks like the attached image. Plus: 
  * Input text area for forecast time range. 
  * Error validation of Search term and forecast time range.
* UnitTests
* AcceptanceTests
* Exception handling
* Caching handling
* Scaling: change the size of items on the device's screen
* ReadMe (this file)

## Architecture & Techniques
* Architecture: Clean Architecture
* Main principles: SOLID principles
* Pattern: MVVM, Repository
* Important techniques and libraries: ReactiveX (with RxJava), Dependency Injection (with Dagger 2), Networking with Retrofit, Caching with Room DB.

## Project structure
Main packages:
* `domain`: defines the domain layer that contains the business logic of the app, including:
	* Entity: the main entities of the app.
	* Use-case: the use-cases that can happen within the app (for example, get weather)
	* Repository interface: used by the use-cases to access the data
	
* `presentation`: defines the presentation layer. This layer communicates with the `domain` layer to modify or retrieve data and transform them into displayable data. This package includes:
	* View Model
	* Activity

* `view`: defines the view layer, including
	* UI model: the displayable data that can be set directly into the view components. UI models are created from the entities by the `presentation` layer.
	* Adapter
	
* `datasource`: defines the implementation of the repository and its gateways.
	* Repository implementation
	* Local data source: the local gateway of the repository. This gateway uses the local database to store and retrieve data.
	* Remote data source: the remote gateway of the repository. This gateway retrieves data from the server via API calls.
	
* `di`: setups the dependency injection of the app using Dagger 2

Other packages:
* `threading`: defines the RxJava schedulers provider
* `utils`: defines app constants, extension functions, and Retrofit API service generator.

## Main libraries
* RxJava
* Retrofit
* Dagger 2
* OkHttp
* Gson
* Android architecture components
* Mockito

## Setup project
To run the app
```
Clone or download the repository from Github.
Open project from MyWeather folder with Android Studio.
Sync project with Gradle files.
Clean and rebuild project.
Run the app on your device or emulator.
```

To run the unit tests
```
Sync project with Gradle files.
Clean and rebuild project.
Right click on test package (com.phuong.myweather(test)).
Choose "Run 'Tests in 'myweather''" to run all test cases.
Choose "Run 'Tests in 'myweather'' with coverage" to run all test cases and see coverage report.
```

## Requisites
* This app requires Android 6.0 or newer
* You must turn on your internet connection at least once to be able to load data from the server
