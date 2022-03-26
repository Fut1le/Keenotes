#### :exclamation: IMPORTANT ANNOUNCEMENT: LIBRARY NOT MAINTAINED :exclamation:

This library is no longer actively maintained, the last commit on it was on *Feb 2, 2016*. This is mainly due to the official APIs for `RecyclerView` added in versions 24.1.0 which supersede most of the implementations in this library. You can check out the Medium posts by @iPaulPro ([Part 1](https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf) and [Part 2](https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-6a6f0c422efd)) for an introduction to those, including some customization options. It is highly recommended to switch to those official APIs, but if you still want to contribute to this project I'll accept pull requests. Happy coding!

---

[![Release](https://img.shields.io/github/release/TR4Android/Swipeable-RecyclerView.svg?label=JitPack%20Maven)](https://jitpack.io/#TR4Android/Swipeable-RecyclerView) [![API](https://img.shields.io/badge/API-7%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=7) [![License](https://img.shields.io/badge/license-Apache 2.0-brightgreen.svg?style=flat)](https://github.com/TR4Android/Swipeable-RecyclerView/blob/master/LICENSE.md)

# Swipeable-RecyclerView
A library that provides an easy and customizable way to implement a swipe to dismiss pattern with RecyclerView and works back to API level 7.

**Note:** This library is currently in active development and might thus not be suitable for production versions as of yet. If you are comfortable experimenting with this library though feel free to give it a spin and report any issues you find. A list of issues currently on the roadmap can be found [here](https://github.com/TR4Android/Swipeable-RecyclerView/issues).

Here's how the demo application looks and behaves (you can download a debug apk of the demo [here](https://github.com/TR4Android/Swipeable-RecyclerView/releases/download/0.2.0/sample-debug.apk)):

![Demo GIF](https://raw.githubusercontent.com/TR4Android/Swipeable-RecyclerView/master/art/demo.gif)

## Usage
### Importing the library

This library is available as a gradle dependency via [JitPack.io](https://github.com/jitpack/jitpack.io). Just add the following lines to your app module `build.gradle`:
``` gradle
repositories { 
    maven { url "https://jitpack.io" }
}
dependencies {
    compile 'com.github.TR4Android:Swipeable-RecyclerView:0.2.0'
}
```

### Code Setup

For a full example check out the implementation of the [SampleAdapter](https://github.com/TR4Android/Swipeable-RecyclerView/blob/master/sample/src/main/java/com/tr4android/recyclerviewslideitemsample/SampleAdapter.java) in the sample folder of this repository.

To be able to use the swipe to dismiss pattern in your RecyclerView you'll have to extend the `SwipeAdapter` in your Adapter class. After that there are only a few minor changes you have to do to get everything going:

##### Migrating from normal adapter
Override `onCreateSwipeViewHolder(ViewGroup parent, int viewType)` and `onBindSwipeViewHolder(ViewHolder holder, int position)` instead of the usual `onCreateViewHolder(ViewGroup parent, int viewType)` and `onBindViewHolder(ViewHolder holder, int position)`. This is needed to wrap your list item in a ViewGroup that handles swiping (namely `SwipeItem`) and handle its configuration. In additon to that you'll also have to replace the boolean `attachToRoot` with `true` so your list item gets attached to the wrapping SwipeItem.

A full implementation might look something like this:
``` java
public class SampleAdapter extends SwipeAdapter {
    ...
    @Override
    public RecyclerView.ViewHolder onCreateSwipeViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sample, parent, true);
        SampleViewHolder sampleViewHolder = new SampleViewHolder(v);
        return sampleViewHolder;
    }
    
    @Override
    public void onBindSwipeViewHolder(RecyclerView.ViewHolder holder, int position) {
        // handle data
    }
    ...
}
```

##### Special setup for swipeable adapter

There also are some new methods related to the swiping pattern in the `SwipeAdapter` that you'll have to override. Those are:

* `onCreateSwipeConfiguration(Context context, int position)`: This is used to determine the configuration of a particular list item and allows flexible control on a per item basis. You'll have to return a `SwipeConfiguration` using the built in `Builder` class. More customization options can be found in the SwipeConfiguration section below.
* `onSwipe(int position, int direction)`: This gets called whenever an item is removed using a swipe. Be sure to call `notifyItemRemoved(position)` there after changing your data to properly allow removal using the default ItemAnimator of the RecyclerView. `int direction` is one of either `SWIPE_LEFT` or `SWIPE_RIGHT` indicating the direction in which the user has dismissed the item.

An implementation might look like this:
``` java
public class SampleAdapter extends SwipeAdapter {
    ...
    public SampleAdapter() {
        // retrieve your data
    }
    ...
    @Override
    public SwipeConfiguration onCreateSwipeConfiguration(Context context, int position) {
        return new SwipeConfiguration.Builder(context)          
            .setBackgroundColorResource(R.color.color_delete)
            .setDrawableResource(R.drawable.ic_delete_white_24dp)
            .build();
    }
    
    @Override
    public void onSwipe(int position, int direction) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
    ...
}
```

##### Special setup for item layout

There's practically no setup needed for the layout itself, but you'll probably want to add a background to the root of your item. If you just want to use the window background add `android:background="?android:attr/windowBackground"`.

### Customization

You can easily customize the actions when swiping by using the `SwipeConfiguration` class which gives you full control over various aspects of this library. The following is a list of all currently available options. For all those there is also a corresponding `setLeft...()` and `setRight...()` flavor.

* `setBackgroundColor(int color)`: The background color that appears behind the list item.
* `setBackgroundColorResource(int resId)`: The resource id of the background color that appears behind the list item.
* `setDrawableResource(int resId)`: The resource id of the drawable shown as a hint for the action.
* `setDescriptionTextColor(int color)`: The text color used for the description and undo text.
* `setDescriptionTextColorResource(int resId)`: The resource id of the text color used for the description and undo text.
* `setDescription(CharSequence description)`: The text shown as a hint for the action.
* `setDescriptionResource(int resId)`: The resource id of the text shown as a hint for the action.
* `setUndoDescription(CharSequence description)`: The text shown when the user has dismissed the item and is shown the option to undo the dismissal.
* `setUndoDescriptionResource(int resId)`: The resource id of the text shown when the user has dismissed the item and is shown the option to undo the dismissal.
* `setUndoable(boolean undoable)`: Whether the action is undoable. If set to `true` the user will have the option to undo the action for 5 seconds, if set to `false` the item will be dismissed immediately.
* `setSwipeBehaviour(SwipeBehavior swipeBehavior)`: The behaviour of the item when swiping. Takes one of the provided default behaviours `NORMAL_SWIPE`, `RESTRICTED_SWIPE` or `NO_SWIPE`.
* `setSwipeBehaviour(float range, Interpolator interpolator)`: The more customized behaviour of the item when swiping, where `range` indicates how far the item can be swiped (percentage of item width) and `interpolator` is the custom Interpolator used when calculating the item position while swiping.
* `setCallbackEnabled(boolean enabled)`: Whether the swipe callback should be triggered on this action. If set to `true` you will receive a swipe action through `onSwipe(int position, int direction)`, if set to `false` you won't.

## License

Copyright 2015 Thomas Robert Altstidl

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## License of RecyclerView and AppCompat v4

Copyright 2015 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
