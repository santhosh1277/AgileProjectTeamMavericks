import { render, screen } from '@testing-library/react';
import ProtectedRoute from './ProtectedRoute';

// Mock react-router-dom
const mockNavigate = jest.fn();
jest.mock(
  'react-router-dom',
  () => ({
    Navigate: ({ to }) => {
      mockNavigate(to);
      return null;
    },
  }),
  { virtual: true }
);

describe('ProtectedRoute Component', () => {
  beforeEach(() => {
    // Clear localStorage before each test
    localStorage.clear();
    jest.clearAllMocks();
  });

  test('redirects to login when not authenticated', () => {
    localStorage.setItem('isAuthenticated', 'false');
    
    render(
      <ProtectedRoute>
        <div>Protected Content</div>
      </ProtectedRoute>
    );

    // Should redirect to login
    expect(mockNavigate).toHaveBeenCalledWith('/login');
    // Should not render protected content
    expect(screen.queryByText('Protected Content')).not.toBeInTheDocument();
  });

  test('renders children when authenticated', () => {
    localStorage.setItem('isAuthenticated', 'true');
    
    render(
      <ProtectedRoute>
        <div>Protected Content</div>
      </ProtectedRoute>
    );

    // Should not redirect
    expect(mockNavigate).not.toHaveBeenCalled();
    // Should render protected content
    expect(screen.getByText('Protected Content')).toBeInTheDocument();
  });

  test('redirects when isAuthenticated is null', () => {
    localStorage.removeItem('isAuthenticated');
    
    render(
      <ProtectedRoute>
        <div>Protected Content</div>
      </ProtectedRoute>
    );

    // Should redirect to login
    expect(mockNavigate).toHaveBeenCalledWith('/login');
    // Should not render protected content
    expect(screen.queryByText('Protected Content')).not.toBeInTheDocument();
  });
});
