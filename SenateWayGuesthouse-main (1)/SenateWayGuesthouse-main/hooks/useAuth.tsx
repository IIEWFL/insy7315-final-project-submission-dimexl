import { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { 
  User, 
  signInWithEmailAndPassword, 
  signOut as firebaseSignOut,
  onAuthStateChanged 
} from 'firebase/auth';
import { auth } from '../firebaseConfig';

// Define the shape of our authentication context
interface AuthContextType {
  user: User | null; // Currently signed-in user, or null if not signed in
  loading: boolean; // Indicates if auth state is still loading
  signIn: (email: string, password: string) => Promise<void>; // Function to sign in
  signOut: () => Promise<void>; // Function to sign out
}

// Create the context with an undefined default
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Provider component that wraps the app and provides auth state
export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null); // State to hold the user
  const [loading, setLoading] = useState(true); // State to track loading status

  // Subscribe to Firebase auth state changes
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setUser(user); // Update user state when auth changes
      setLoading(false); // Auth state finished loading
    });

    // Cleanup subscription when component unmounts
    return () => unsubscribe();
  }, []);

  // Function to sign in a user with email and password
  const signIn = async (email: string, password: string) => {
    await signInWithEmailAndPassword(auth, email, password);
  };

  // Function to sign out the current user
  const signOut = async () => {
    await firebaseSignOut(auth);
  };

  // Value that will be provided to the context consumers
  const value = {
    user,
    loading,
    signIn,
    signOut,
  };

  // Wrap children with the AuthContext provider
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// Custom hook to easily access the auth context in components
export function useAuth() {
  const context = useContext(AuthContext);

  // Ensure the hook is used within the provider
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }

  return context;
}

