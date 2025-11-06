import { afterEach, beforeAll, vi } from 'vitest';
import { cleanup } from '@testing-library/react';
import '@testing-library/jest-dom';

// Mock window.matchMedia for theme detection
beforeAll(() => {
  Object.defineProperty(window, 'matchMedia', {
    writable: true,
    value: vi.fn().mockImplementation((query: string) => {
      return {
        matches: false,
        media: query,
        onchange: null,
        addListener: vi.fn(), // deprecated
        removeListener: vi.fn(), // deprecated
        addEventListener: vi.fn(),
        removeEventListener: vi.fn(),
        dispatchEvent: vi.fn(),
      };
    }),
  });

  // Mock window.location for security tests and App component
  // Using delete and reassign to properly override jsdom's location object
  delete (window as any).location;
  (window as any).location = {
    protocol: 'https:',
    hostname: 'localhost',
    pathname: '/',
    href: 'https://localhost/',
    hash: '',
    search: '',
    assign: vi.fn(),
    replace: vi.fn(),
    reload: vi.fn(),
  };

  // Ensure localStorage is available and clean
  //user2025469 (2013). Check if localStorage is available. [online] Stack Overflow. Available at: https://stackoverflow.com/questions/16427636/check-if-localstorage-is-available.
  if (typeof Storage === 'undefined') {
    const storage: { [key: string]: string } = {};
    (global as any).localStorage = {
      getItem: (key: string) => storage[key] || null,
      setItem: (key: string, value: string) => {
        storage[key] = value;
      },
      removeItem: (key: string) => {
        delete storage[key];
      },
      clear: () => {
        Object.keys(storage).forEach(key => delete storage[key]);
      },
    };
  }
  
  // Mock window.alert for form submission tests
  //Mayur Dhurpate (2019). How to mock or assert whether window.alert has fired in React & Jest with typescript? [online] Stack Overflow. Available at: https://stackoverflow.com/questions/55933105/how-to-mock-or-assert-whether-window-alert-has-fired-in-react-jest-with-typesc.
  window.alert = vi.fn();
  
  // Clear localStorage before each test run
  //Chiedo (2015). How do I deal with localStorage in jest tests? [online] Stack Overflow. Available at: https://stackoverflow.com/questions/32911630/how-do-i-deal-with-localstorage-in-jest-tests.
  localStorage.clear();
});

// Cleanup after each test case
afterEach(() => {
  cleanup();
  // Clear localStorage after each test
  //www.w3schools.com. (n.d.). Storage clear() Method. [online] Available at: https://www.w3schools.com/JSREF/met_storage_clear.asp.
  localStorage.clear();
});

