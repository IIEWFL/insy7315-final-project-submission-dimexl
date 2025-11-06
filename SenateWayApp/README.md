# Senate Way Guesthouse - Android Native App

This is the native Android version of the Senate Way Guesthouse Capacitor app, converted from React/TypeScript to pure Kotlin and XML.

## Project Structure

```
app/
├── src/main/
│   ├── java/com/senateway/guesthouse/
│   │   ├── MainActivity.kt              # Main activity with navigation
│   │   ├── config/
│   │   │   └── FirebaseConfig.kt         # Firebase configuration
│   │   ├── data/model/                  # Data models
│   │   ├── ui/
│   │   │   ├── home/                    # Home/Hero/About screen
│   │   │   ├── rooms/                   # Rooms listing
│   │   │   ├── gallery/                 # Photo gallery
│   │   │   ├── reviews/                 # Guest reviews
│   │   │   ├── map/                     # Location map
│   │   │   ├── contact/                 # Contact/Booking form
│   │   │   ├── chatbot/                 # AI assistant
│   │   │   └── admin/                   # Admin dashboard
│   │   └── res/
│   │       ├── layout/                  # XML layouts
│   │       ├── values/                  # Resources (strings, colors, themes)
│   │       └── navigation/              # Navigation graph
```

## Features Converted

✅ **Main Navigation** - Bottom navigation bar with all main sections
✅ **Home Screen** - Hero section with image, features, and about section
✅ **Rooms** - Room listing with filtering (basic structure)
✅ **Contact/Booking** - Contact form with Firebase integration
✅ **Gallery** - Photo gallery (basic structure)
✅ **Reviews** - Guest reviews display (basic structure)
✅ **Map** - Google Maps integration showing guesthouse location
✅ **Chatbot** - AI assistant interface (basic structure)
✅ **Admin** - Admin login and dashboard (basic structure)
✅ **Firebase** - Realtime Database and Authentication setup

## Setup Instructions

1. **Configure Firebase:**
   - Replace `app/google-services.json` with your actual Firebase configuration file
   - Update Firebase settings in `FirebaseConfig.kt` if needed

2. **Configure Google Maps:**
   - Add your Google Maps API key to `app/src/main/res/values/strings.xml`:
     ```xml
     <string name="google_maps_key">YOUR_API_KEY_HERE</string>
     ```

3. **Add Images:**
   - Place room and gallery images in `app/src/main/res/drawable/` or `res/mipmap/`
   - Update image references in Room data models and layouts

4. **Build and Run:**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

## Dependencies

- AndroidX (AppCompat, Navigation, Lifecycle, etc.)
- Material Design Components
- Firebase (Database, Auth, Analytics)
- Google Maps SDK
- Glide (Image loading)
- Retrofit (for API calls)
- Coroutines

## Next Steps / To Complete

1. **Gallery Implementation:**
   - Complete RecyclerView adapter for gallery images
   - Add image viewer/detail screen
   - Load images from Firebase Storage or local resources

2. **Reviews Implementation:**
   - Complete RecyclerView adapter for reviews
   - Add review submission form
   - Implement rating display with stars

3. **Chatbot Implementation:**
   - Integrate Gemini AI API
   - Complete message RecyclerView adapter
   - Add typing indicators and message handling

4. **Rooms Enhancement:**
   - Add filtering UI (size, capacity, price)
   - Implement room detail screen
   - Add image loading with Glide

5. **Admin Dashboard:**
   - Complete Analytics screen
   - Complete Bookings management screen
   - Add charts and statistics

6. **Theme Support:**
   - Implement dark mode theme toggle
   - Add theme preference storage

7. **Image Loading:**
   - Set up Glide properly for all images
   - Add placeholder and error handling
   - Consider Firebase Storage for dynamic images

8. **Navigation:**
   - Enhance navigation with proper back stack handling
   - Add navigation drawer if needed

## Notes

- All React/TypeScript components have been converted to Kotlin Activities/Fragments
- Material Design 3 is used for UI components
- Firebase Realtime Database replaces the web Firebase setup
- Navigation uses Android Navigation Component
- The app structure follows Android best practices and Material Design guidelines

## Original Capacitor App Location

Original source code location: `C:\Users\HP\Downloads\SenateApp`

