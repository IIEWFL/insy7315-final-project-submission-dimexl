import { Button } from './ui/button';
import { Star, Wifi, Utensils, Car } from 'lucide-react';
import { ImageWithFallback } from './figma/ImageWithFallback';

interface HeroProps {
  onNavigate: (page: string) => void; // Function to handle navigation between pages
}

export function Hero({ onNavigate }: HeroProps) {
  return (
    <div className="relative">
      {/* ===== Hero Section ===== */}
      <div className="relative h-[500px] overflow-hidden">
        {/* Background image with fallback support */}
        <ImageWithFallback
          src='/images/Senate1.jpg'
          alt="SenateWay Guesthouse"
          className="w-full h-full object-cover"
        />
        {/* Dark gradient overlay for text visibility */}
        {/*GeeksforGeeks (2024a). How to Add a Gradient Overlay to Text with CSS? [online] GeeksforGeeks. Available at: https://www.geeksforgeeks.org/css/how-to-add-a-gradient-overlay-to-text-with-css/ */}
        <div className="absolute inset-0 bg-gradient-to-r from-black/60 to-black/30" />
        
        {/* Hero text content */}
        <div className="absolute inset-0 flex items-center">
          <div className="container mx-auto px-4">
            <div className="max-w-2xl text-white">
              <h1 className="text-5xl mb-4 text-white">
                Welcome to Senate Way Guesthouse
              </h1>
              <p className="text-xl mb-6 text-white">
                Experience comfort and hospitality in the heart of Kimberley. 
                Your perfect stay awaits with modern amenities and exceptional service.
              </p>

              {/* Action buttons for navigation */}
              <div className="flex flex-wrap gap-4">
                <Button size="lg" onClick={() => onNavigate('rooms')}>
                  View Our Rooms
                </Button>
                <Button
                  size="lg"
                  variant="outline"
                  className="bg-white/10 text-white border-white hover:bg-white/20"
                  onClick={() => onNavigate('contact')}
                >
                  Book Now
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* ===== Quick Features Section ===== */}
      <div className="bg-primary text-white">
        <div className="container mx-auto px-4 py-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            
            {/* Feature: Free WiFi */}
            <div className="flex items-center gap-3">
              <Wifi className="w-8 h-8" />
              <div>
                <p className="text-white">Free WiFi</p>
                <p className="text-sm text-white/80">High-Speed Internet</p>
              </div>
            </div>

            {/* Feature: Free Parking */}
            <div className="flex items-center gap-3">
              <Car className="w-8 h-8" />
              <div>
                <p className="text-white">Free Parking</p>
                <p className="text-sm text-white/80">On-Site Private</p>
              </div>
            </div>

            {/* Feature: BBQ Facilities */}
            <div className="flex items-center gap-3">
              <Utensils className="w-8 h-8" />
              <div>
                <p className="text-white">BBQ Facilities</p>
                <p className="text-sm text-white/80">Outdoor Area</p>
              </div>
            </div>

            {/* Feature: Guest Favorite */}
            <div className="flex items-center gap-3">
              <Star className="w-8 h-8" />
              <div>
                <p className="text-white">Top Rated</p>
                <p className="text-sm text-white/80">Guest Favorite</p>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  );
}
