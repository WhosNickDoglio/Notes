# Notes

[![Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![Build Status](https://travis-ci.org/WhosNickDoglio/Notes.svg?branch=master)](https://travis-ci.org/WhosNickDoglio/Notes)
[![codecov](https://codecov.io/gh/WhosNickDoglio/Notes/branch/master/graph/badge.svg)](https://codecov.io/gh/WhosNickDoglio/Notes)

A simple Material Design note taking Android application written entirely in Kotlin!

<a href="https://play.google.com/store/apps/details?id=com.nicholasdoglio.notes">
    <img alt="Get it on Google Play" height="80" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />
</a>


<img src="https://i.imgur.com/eLiXBD0.png" width="250" height="450"> <img src="https://i.imgur.com/lhMCQ8z.png" width="250" height="450"> <img src="https://i.imgur.com/pfZjgBF.png" width="250" height="450">



# Features 

- Create/Edit/Delete notes 
- Quickly create a note from a Quick Tile Setting (7.0+) or a App Shortcut (7.1+)

# To-Do

All planned work is detailed in the [projects](https://github.com/WhosNickDoglio/Notes/projects) page.

# Changelog

All the releases and change logs are kept under [releases](https://github.com/WhosNickDoglio/Notes/releases).

# Libraries 

- [Android Support](https://developer.android.com/topic/libraries/support-library/index.html)
- [Anko-Commons](https://github.com/Kotlin/anko)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html) 
  - [Paging](https://developer.android.com/topic/libraries/architecture/paging.html)
  - [Room](https://developer.android.com/topic/libraries/architecture/room.html)
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html) 
- [RxJava](https://github.com/ReactiveX/RxJava)
- [RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [RxBinding](https://github.com/JakeWharton/RxBinding)
- [AutoDispose](https://github.com/uber/AutoDispose)
- [Dagger](https://google.github.io/dagger/) 


# Testing
- [JUnit](http://junit.org/junit5/)
- [Robolectric](http://robolectric.org/)
- [Espresso](https://developer.android.com/training/testing/espresso/index.html)


# Presentation Architecture 

The Presentation layer was written using the Model-View-ViewModel (MVVM) pattern. Each `Fragment` is given it's own `ViewModel` which emits data for the Fragment to observe.


This application also uses a single activity with multiple fragments architecture. 


# Feedback

Bugs found? Suggestions? Requests?

Feel free to contact me or open up a issue:

[Twitter](https://twitter.com/WhosNickDoglio)

[Email](mailto:NicholasDoglio@Gmail.com)

# License

        Copyright (C) 2017  Nicholas Doglio

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.