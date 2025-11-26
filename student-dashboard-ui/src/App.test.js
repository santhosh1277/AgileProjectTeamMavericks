import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

// Mock react-router-dom
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  BrowserRouter: ({ children }) => <div>{children}</div>,
  Route: ({ element }) => element,
  Routes: ({ children }) => <div>{children}</div>,
  useLocation: () => ({ pathname: '/' }),
  useNavigate: () => mockNavigate,
}), { virtual: true });

// Mock all route components
jest.mock('./HomePage/Home', () => () => <div>Home Page</div>);
jest.mock('./Signup Page/Signup', () => () => <div>Signup Page</div>);
jest.mock('./LoginPage/Login', () => () => <div>Login Page</div>);
jest.mock('./Side-Nav-Bar/SideNav', () => () => <div>SideNav</div>);
jest.mock('./DashboardHome/DashboardHome', () => () => <div>Dashboard</div>);
jest.mock('./DashboardHome/UserProfile', () => () => <div>Profile</div>);
jest.mock('./DashboardHome/CourseDetails', () => () => <div>CourseDetails</div>);
jest.mock('./components/ProtectedRoute', () => ({ children }) => <div>{children}</div>);

describe('App Component', () => {
  test('renders without crashing', () => {
    render(<App />);
    expect(screen.getByText('Home Page')).toBeInTheDocument();
  });
});
