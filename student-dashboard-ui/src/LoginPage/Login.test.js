import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Login from './Login';
import * as auth from '../api/auth';

const mockNavigate = jest.fn();

// Provide a minimal mock for react-router-dom to avoid resolver/ESM issues in the test env
// Mock react-router-dom as a virtual module so Jest doesn't attempt to resolve the real package
jest.mock(
	'react-router-dom',
	() => ({
		useNavigate: () => mockNavigate,
		BrowserRouter: ({ children }) => children,
	}),
	{ virtual: true }
);

jest.mock('../api/auth', () => ({
	login: jest.fn(),
}));

beforeEach(() => {
	localStorage.clear();
	jest.clearAllMocks();
});

test('successful login stores user and navigates to dashboard', async () => {
	auth.login.mockResolvedValue({ id: 1, name: 'Test User' });

	render(<Login />);

	fireEvent.change(screen.getByPlaceholderText(/Enter your email or username/i), {
		target: { name: 'usernameOrEmail', value: 'testuser' },
	});
	fireEvent.change(screen.getByPlaceholderText(/Enter your password/i), {
		target: { name: 'password', value: 'password' },
	});

	fireEvent.click(screen.getByRole('button', { name: /login/i }));

	await waitFor(() => {
		expect(auth.login).toHaveBeenCalledWith({
			usernameOrEmail: 'testuser',
			password: 'password',
		});
	});

	await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith('/dashboard/home'));
	expect(localStorage.getItem('isAuthenticated')).toBe('true');
});

test('shows error message on failed login', async () => {
	auth.login.mockRejectedValue(new Error('Invalid credentials'));

	render(<Login />);

	fireEvent.change(screen.getByPlaceholderText(/Enter your email or username/i), {
		target: { name: 'usernameOrEmail', value: 'wrong' },
	});
	fireEvent.change(screen.getByPlaceholderText(/Enter your password/i), {
		target: { name: 'password', value: 'wrong' },
	});

	fireEvent.click(screen.getByRole('button', { name: /login/i }));

	await waitFor(() => {
		expect(screen.getByRole('alert')).toHaveTextContent(/invalid credentials/i);
	});
});
