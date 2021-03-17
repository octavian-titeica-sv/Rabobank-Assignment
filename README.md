## Architecture

# UI module
  - Contains app views (MainActivity and MainFragment) and the components involved in displaying the users (UsersAdapter
  and UserItemView).
  - Dagger components used for dependency injection are placed in this module.

# Presentation
  - Is the layer that interacts with the UI.
  - It contains the ViewModel.
  - ViewModel receives data from the usecase and exposes that data as a state to the UI using LiveData.
  - Each response received by the view model is mapped to a relevant state, represented by UserState data class.
  - It contains UserState data class, which will determine view state.

# Domain
  - Contains business logic organized in use cases.
  - Every single business rule is represented by a single use case. This ensures scalability, maintainability and
  testability.
  - This layer is using dependency inversion. GetUsersUseCase depend on FileParserRepository abstraction rather than
  concrete implementation.
  - For simplicity, a single model was used for the user (UserModel), model located in domain module.
  - Every result is exposed to presentation layer by UserResponse data class.
  - Also for simplicity reasons, interactors are not present in the app architecture, so the data received from
  data layer is transformed into UserResponse directly into the use case.

# Data
  - This layer holds the implementation of interfaces defined in domain layer: FileParserRepositoryImpl. This class
  emits the result upstream.
  - This layer contains the classes used to read and parse an input file.
  - CSVFileReader class reads an input file and exposes its contents as a list of strings, each element of the list
  representing a line from the provided input file.
  - UserBuilder class holds the purpose of building an user based on a line provided by CSVFileReader. As its name
  suggests, this class relies on builder pattern.

# Base
  - This module holds exceptions and util classes.
  - CSVParsingException is the specific exception thrown by CSVFileReader.
  - util package consists in a constants class, DateConverter utility class and also defines some TypeAliases for
  simplicity (MutableList<List<T>> as MutableListOfList<T> and List<List<T>> as ListOfList<T>).
