/* 
 * Copyright 2017 Augusto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here, other Firebase libraries
// are not available in the service worker.
importScripts('https://www.gstatic.com/firebasejs/4.1.3/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/4.1.3/firebase-messaging.js');

// Initialize the Firebase app in the service worker by passing in the
// messagingSenderId. Exactly same way in script of your page.
var config = {
    apiKey: "AIzaSyAx7ObYTyyFov4pDB2bewQczHgVoHbpXYM",
    authDomain: "testandopushnotification-99d66.firebaseapp.com",
    databaseURL: "https://testandopushnotification-99d66.firebaseio.com",
    projectId: "testandopushnotification-99d66",
    storageBucket: "testandopushnotification-99d66.appspot.com",
    messagingSenderId: "167374573198"
};
firebase.initializeApp(config);

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging();


