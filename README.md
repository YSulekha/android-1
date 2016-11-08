# Project - *Gripsack*

**Gripsack** is an android app that allows a user to plan trips, assist a user during their active trip and share completed trips with friends. 

## User Stories

The following **required** functionality is completed:

* User can act on places
* [] A User can sign in to the app with facebook or google
* [] A User can send invitation to their contacts to use an App

* [] Users can explore places near by
  * [] Users can search place by type, name or address.
  * [] Users can add place to their bucketlist. Bucketlist is a list of places users must visit.
  * [] Users can like place
  * [] Users can checkin to a place and mark it as visited once user is located near the place.
  * [] Users can recommend place to their friends once user checked in.

* [] Users can view the list of places using the following tabs: 
  * [] **Bucketlist** Shows the list of places user must visit.
    * [] User can remove place 
    * [] User can add new place
  * [] **Liked** Shows the list of places user liked.
    * [] User can unlike place by removing place from the list
    * [] User can add place to the bucketlist
  * [] **Recommended** Users can see the list of places recommended by their friends or an app.
      * [] User can hide place by removing place from the list
      * [] User can add place to the bucketlist
      * [] User can like place
  * [] **Visited** Users can see the list of places they visited.
      * [] User can recommend place to their friends
      * [] User can like place
  
* [] User can create new trip:
  * [] User can provide starting/ending dates, starting and ending points and trip type (business, personal)
  * [] User can add places to the trip by using recommendation engine of the app
  
* [] User can edit trip info:
  * [] User can change starting/ending dates, starting and ending points and trip type (business, personal)
  * [] User can add/remove places to the trip by using recommendation engine of the app

* [] User can view trip info:
  * [] User can check in to place near by
  * [] User can take photo and it should create check point 
  
* [] Users can view the list of their trips using the following tabs: 
  * [] **Upcoming** Shows the list of upcoming trips.
    * [] Active trip should have distinct look and provide useful info 
    * [] User can launch trip editor by clicking on the trip
  * [] **Completed** Shows the list of completed trips.
    * [] User can share trip with their friends
    * [] User can archive trip
    * [] User can launch trip viewer by clicking on the trip
  * [] **Maps** Users can see the list of maps for all their trips.
      * [] User can share map with others
      * [] User can view map by clicking on item 
  * [] **Photos** Users can see the list of photos grouped by trip.
      * [] User can share trip photos with others
      * [] User can open photo galary viewer by clicking on items
      
      
The following **optional** features are implemented:

* [] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [] Replaced Filter Settings Activity with a lightweight modal overlay
* [] Improved the user interface and experiment with image assets and/or styling and coloring

The following **bonus** features are implemented:

* [] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* [] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [] Uses [retrolambda expressions](http://guides.codepath.com/android/Lambda-Expressions) to cleanup event handling blocks.
* [] Leverages the popular [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.
* [] Leverages the [Retrofit networking library](http://guides.codepath.com/android/Consuming-APIs-with-Retrofit) to access the New York Times API.
* [] Replace the embedded `WebView` with [Chrome Custom Tabs](http://guides.codepath.com/android/Chrome-Custom-Tabs) using a custom action button for sharing. (_**2 points**_)

 
## Wireframes 

[First Draft](https://github.com/gripsack/android/blob/master/wireframes.pdf?raw=true)


## Video Walkthrough

Here's a walkthrough of implemented user stories:


## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used
