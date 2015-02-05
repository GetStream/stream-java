stream-java (in progress)
=========================

**Bugs:**

there is a problem parsing back the response in case of "addActivity" since I saw that the response object in the "to" field contains a nested structure (slug + key), when I try to serialize the json to the object it failed since it doesn't know that structure. That's quite annoying since the activity is something that can be created by the user, he can do that by simply extending an object: "MyActivity extends BaseActivity", the field "to" is then inherited, but according to your interface the "to" field is serialized in a way and deserialized in an different one. It makes my life harder since I cannot use the "MyActivity" object created by the user and it force me to implement a manual deserialization.

(Known) missing features/to do list:

* bulk operations (should be easy)
* java docs
* unit tests
