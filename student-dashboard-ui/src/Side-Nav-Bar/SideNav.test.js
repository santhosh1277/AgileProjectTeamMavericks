import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import SideNav from './SideNav';

// Mock useNavigate and Link
const mockNavigate = jest.fn();

jest.mock(
  'react-router-dom',
  () => ({
    useNavigate: () => mockNavigate,
    Link: ({ children, to }) => <a href={to}>{children}</a>,
  }),
  { virtual: true }
);

describe('SideNav Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
  });

  test('renders navigation links', () => {
    render(<SideNav />);
    
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Home')).toBeInTheDocument();
    expect(screen.getByText('Profile')).toBeInTheDocument();
    expect(screen.getByText('Logout')).toBeInTheDocument();
  });

  test('displays user name when available', () => {
    localStorage.setItem('user', JSON.stringify({ name: 'John Doe' }));
    
    render(<SideNav />);
    
    expect(screen.getByText('John')).toBeInTheDocument();
  });

  test('handles logout correctly', () => {
    localStorage.setItem('user', JSON.stringify({ name: 'John Doe' }));
    localStorage.setItem('isAuthenticated', 'true');
    
    render(<SideNav />);
    
    const logoutButton = screen.getByText('Logout');
    fireEvent.click(logoutButton);
    
    expect(localStorage.getItem('user')).toBeNull();
    expect(localStorage.getItem('isAuthenticated')).toBeNull();
    expect(mockNavigate).toHaveBeenCalledWith('/');
  });

  test('handles missing user name gracefully', () => {
    localStorage.setItem('user', JSON.stringify({ email: 'test@example.com' }));
    
    render(<SideNav />);
    
    // Should render without crashing even without name
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
  });
});
