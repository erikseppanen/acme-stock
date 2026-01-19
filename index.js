import { registerRootComponent } from 'expo';

// Load the ClojureScript app - this runs acme.app/init
require('./app/index.js');

// Get the app component that was set by init
const app = global.acmeApp;

// Register the app
registerRootComponent(app);
