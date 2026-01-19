# Acme Stock & Shopping List

A cross-platform mobile app for managing household stock inventory and shopping lists, built with ClojureScript and React Native.

## Features

- **Stock Inventory Management** - Track items you have at home with quantities and categories
- **Shopping List** - Maintain a list of items you need to buy
- **Cross-Platform** - Runs on iOS, Android, and Web from a single codebase
- **Real-time Sync** - Data synced across devices via Firebase Firestore
- **Offline Support** - Works offline with automatic sync when back online

## Tech Stack

- **ClojureScript** - Functional programming language that compiles to JavaScript
- **shadow-cljs** - ClojureScript build tool with first-class npm integration
- **Expo** - React Native framework for cross-platform development
- **Reagent** - ClojureScript interface to React
- **re-frame** - State management pattern for Reagent apps
- **Malli** - Data validation with schemas
- **Firebase Firestore** - Cloud database with real-time sync

## Prerequisites

- Node.js >= 18
- npm or yarn
- Java JDK 11+ (for ClojureScript compilation)
- Xcode (for iOS development)
- Android Studio (for Android development)
- [Watchman](https://facebook.github.io/watchman/) (recommended for macOS)

## Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/acme-stock.git
   cd acme-stock
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure Firebase**

   Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com) and update the config in `src/main/acme/firebase/config.cljs`:
   ```clojure
   (def firebase-config
     #js {:apiKey "your-api-key"
          :authDomain "your-project.firebaseapp.com"
          :projectId "your-project-id"
          :storageBucket "your-project.appspot.com"
          :messagingSenderId "your-sender-id"
          :appId "your-app-id"})
   ```

4. **Start the ClojureScript compiler**
   ```bash
   npx shadow-cljs watch app
   ```

5. **Start Expo** (in a separate terminal)
   ```bash
   npx expo start
   ```

## Running the App

### Web
```bash
npx expo start --web
```

### iOS Simulator
```bash
npx expo start --ios
```

### Android Emulator
```bash
npx expo start --android
```

## Project Structure

```
src/main/acme/
├── app.cljs                 # App entry point
├── db/
│   ├── core.cljs           # Initial app-db state
│   └── schema.cljc         # Malli schemas
├── events/
│   ├── core.cljs           # Core events (init, UI)
│   ├── stock.cljs          # Stock item events
│   ├── needed.cljs         # Shopping list events
│   └── sync.cljs           # Firebase sync events
├── subs/
│   ├── stock.cljs          # Stock subscriptions
│   └── needed.cljs         # Shopping list subscriptions
├── firebase/
│   ├── config.cljs         # Firebase initialization
│   ├── firestore.cljs      # Firestore CRUD operations
│   └── listeners.cljs      # Real-time listeners
├── fx/
│   └── firebase.cljs       # re-frame effects for Firebase
├── native/
│   └── navigation.cljs     # React Navigation setup
└── ui/
    ├── styles.cljs         # Shared styles
    ├── components/
    │   ├── button.cljs     # Button components
    │   ├── item_card.cljs  # Item card component
    │   └── item_form.cljs  # Form components
    └── screens/
        ├── stock_list.cljs    # Stock inventory screen
        ├── needed_list.cljs   # Shopping list screen
        └── item_detail.cljs   # Add/edit item screen
```

## Development

### REPL

Connect to the shadow-cljs nREPL for interactive development:
```bash
npx shadow-cljs cljs-repl app
```

### Hot Reload

The app supports hot reloading. Changes to ClojureScript files will automatically be reflected in the running app.

## Building for Production

### Web
```bash
npx shadow-cljs release app
npx expo export --platform web
```

### iOS/Android
```bash
npx shadow-cljs release app
npx expo build:ios
npx expo build:android
```

## License

MIT
