# WeightTrackerEnhanced

WeightTrackerEnhanced is an Android weight-tracking application developed with Java, XML, and SQLite. It began as a CS 360 mobile application and was later substantially enhanced during my CS 499 Computer Science Capstone.

The project demonstrates software engineering, algorithms and data structures, relational database design, and security improvements within a single application.

## App Preview

![Main screen](assets/main-screen.png)

![Weight history](assets/history-screen.png)

![Settings and themes](assets/settings-screen.png)

## Features

* Create and log in to user accounts
* Record daily weight entries
* View, update, and delete weight history
* Set a customizable goal weight
* Receive an SMS notification when a goal weight is reached
* Choose between Light, Dark, Forest, and Ocean themes
* Save user preferences between sessions
* View progress calculations and weight trends
* Keep weight records separated by individual user account

## Capstone Enhancements

### Software Design and Engineering

The original application was redesigned to provide a more customizable and consistent user experience.

Improvements included:

* User-selectable themes
* Persistent settings using `SharedPreferences`
* Customizable goal weight
* Improved input validation
* Redesigned Settings screen
* More consistent layouts and navigation

### Algorithms and Data Structures

The weight-history screen was expanded to analyze the user's stored data.

The application now calculates:

* Starting weight
* Current weight
* Total weight change
* Average change between entries
* Upward, downward, or stable weight trends

Weight entries are processed using an `ArrayList`, iteration, comparisons, calculations, and conditional logic.

### Databases and Security

The SQLite database was redesigned so weight records belong to individual users rather than being shared across every account.

Improvements included:

* User IDs associated with weight records
* Foreign-key relationships between users and weights
* User-specific create, read, update, and delete operations
* Password hashing instead of plain-text password storage
* Account-specific access to weight history

## Technologies

* Java
* XML
* SQLite
* Android Studio
* Android SDK
* SharedPreferences
* Git and GitHub

## Project Structure

The application includes separate components for:

* User authentication
* Weight entry
* Weight history
* Settings and preferences
* SQLite database management
* Theme management
* Progress calculations

## Original Project

This repository contains the enhanced version of the application.

The original CS 360 version is preserved separately in the `WeightTrackerPilot` repository so the development and improvements made during the capstone can be compared.

## Skills Demonstrated

This project demonstrates experience with:

* Object-oriented programming
* Android application development
* User interface design
* Relational database design
* CRUD operations
* Algorithms and data processing
* Input validation
* Authentication and data security
* Maintaining and improving an existing codebase

## About

WeightTrackerEnhanced represents the culmination of several areas of my Computer Science program. Rather than building a new application from scratch, I returned to an earlier project, evaluated its limitations, and improved its design, functionality, data handling, and security.
