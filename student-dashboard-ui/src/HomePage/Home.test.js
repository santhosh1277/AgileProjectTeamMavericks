import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import Home from './Home';

// Mock useNavigate
const mockNavigate = jest.fn();
jest.mock(
  'react-router-dom',
  () => ({
    useNavigate: () => mockNavigate,
  }),
  { virtual: true }
);

// Mock Footer component
jest.mock('../components/Footer', () => () => <div>Footer</div>);

// Mock fetch
globalThis.fetch = jest.fn();

describe('Home Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders loading state initially', () => {
    globalThis.fetch.mockImplementation(() => new Promise(() => {}));
    
    render(<Home />);
    expect(screen.getByText(/Loading universities/i)).toBeInTheDocument();
  });

  test('renders universities on successful fetch', async () => {
    const mockData = [
      { id: 1, name: 'Trinity College Dublin', country: 'Ireland' },
      { id: 2, name: 'University College Cork', country: 'Ireland' },
    ];

    globalThis.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockData,
    });

    render(<Home />);

    await waitFor(() => {
      expect(screen.queryByText(/Loading universities/i)).not.toBeInTheDocument();
    });
  });

  test('displays error on fetch failure', async () => {
    globalThis.fetch.mockResolvedValueOnce({
      ok: false,
    });

    render(<Home />);

    await waitFor(() => {
      expect(screen.getByText(/Failed to fetch/i)).toBeInTheDocument();
    });
  });
});
