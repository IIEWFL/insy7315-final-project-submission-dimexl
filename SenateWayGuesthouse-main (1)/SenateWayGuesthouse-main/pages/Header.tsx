import { MapPin, Phone, Mail } from 'lucide-react';
import { Button } from './ui/button';
import { ThemeToggle } from './ui/theme-toggle';

interface HeaderProps {
  currentPage: string;              // Tracks the currently active page
  onNavigate: (page: string) => void; // Function to handle navigation clicks
}//Stack Overflow. (n.d.). How to run a function when user clicks the back button, in React.js? [online] Available at: https://stackoverflow.com/questions/71392600/how-to-run-a-function-when-user-clicks-the-back-button-in-react-js.

export function Header({ currentPage, onNavigate }: HeaderProps) {
  // Navigation menu items
  //W3schools (2019a). CSS Navigation Bar. [online] W3schools.com. Available at: https://www.w3schools.com/css/css_navbar.asp.
  const navItems = [
    { id: 'home', label: 'Home' },
    { id: 'rooms', label: 'Rooms' },
    { id: 'gallery', label: 'Gallery' },
    { id: 'reviews', label: 'Reviews' },
    { id: 'map', label: 'Location' },
    { id: 'contact', label: 'Contact' },
    { id: 'chatbot', label: 'AI Assistant' },
  ];

  return (
    <header className="sticky top-0 z-50 bg-background border-b border-border">
      <div className="container mx-auto px-4">
        {/* ===== Top Contact Bar ===== */}
        <div className="py-2 border-b border-border">
          <div className="flex flex-wrap items-center justify-between gap-4">
            {/* Left side: Address & Phone */}
            <div className="flex items-center gap-6">
              <div className="flex items-center gap-2">
                <MapPin className="w-4 h-4 text-primary" />
                <span className="text-sm text-muted-foreground dark:text-muted-foreground">
                  10 Senate Way, 8345 Kimberley, South Africa
                </span>
              </div>
              <div className="flex items-center gap-2">
                <Phone className="w-4 h-4 text-primary" />
                <span className="text-sm text-muted-foreground dark:text-muted-foreground">
                  +27 82 927 8907
                </span>
              </div>
            </div>

            {/* Right side: Email */}
            <div className="flex items-center gap-2">
              <Mail className="w-4 h-4 text-primary" />
              <span className="text-sm text-muted-foreground dark:text-muted-foreground">
                vanessa141169@yahoo.com
              </span>
            </div>
          </div>
        </div>

        <div className="py-4 flex flex-wrap items-center justify-between gap-4">
          {/* Brand / Logo */}
          <div>
            <h1
              className="text-primary font-semibold cursor-pointer"
              onClick={() => onNavigate('home')} // Navigate to home when clicked
            >
              Senate Way Guesthouse
            </h1>
            <p className="text-sm text-muted-foreground">
              Your Home Away From Home
            </p>
          </div>

          {/* Navigation Links & Theme Toggle */}
          <nav className="flex flex-wrap gap-2 items-center">
            {navItems.map((item) => (
              <Button
                key={item.id}
                variant={currentPage === item.id ? 'default' : 'ghost'} // Highlight active page
                onClick={() => onNavigate(item.id)}
              >
                {item.label}
              </Button>
            ))}

            {/* Toggle for light/dark theme */}
            {/*w3schools (n.d.). How To Toggle Between Dark and Light Mode. [online] www.w3schools.com. Available at: https://www.w3schools.com/howto/howto_js_toggle_dark_mode.asp.*/}
            <ThemeToggle />
          </nav>
        </div>
      </div>
    </header>
  );
}
