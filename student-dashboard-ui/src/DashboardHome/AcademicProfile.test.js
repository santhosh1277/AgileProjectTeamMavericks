import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import CourseRecommendationForm from './AcademicProfile';
import * as StudentService from '../Service/StudentService';

// Mock react-router-dom navigate
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn(),
}));

// Mock ContactDialog component to a simple stub
jest.mock('./UserConsentDailog', () => ({
  __esModule: true,
  default: ({ show, onClose }) => (
    <div data-testid="contact-dialog" style={{ display: show ? 'block' : 'none' }}>
      <button onClick={onClose}>close</button>
    </div>
  ),
}));

// Helper to set localStorage user
const setUserEmail = (email) => {
  // component expects quotes around stored value and removes them
  window.localStorage.setItem('user', JSON.stringify(email));
};

describe('AcademicProfile (CourseRecommendationForm)', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    window.localStorage.clear();
  });

  test('shows consent dialog when getUserConsent returns false', async () => {
    setUserEmail('test@example.com');
    jest.spyOn(StudentService, 'getUserConsent').mockResolvedValue(false);
    jest.spyOn(StudentService, 'getRecommendationsByEmail').mockResolvedValue([]);

    render(<CourseRecommendationForm />);

    const dialog = await screen.findByTestId('contact-dialog');
    expect(dialog).toBeVisible();
  });

  test('hides consent dialog when getUserConsent returns true', async () => {
    setUserEmail('test@example.com');
    jest.spyOn(StudentService, 'getUserConsent').mockResolvedValue(true);
    jest.spyOn(StudentService, 'getRecommendationsByEmail').mockResolvedValue([]);

    render(<CourseRecommendationForm />);

    const dialog = await screen.findByTestId('contact-dialog');
    // dialog is rendered but should be hidden
    await waitFor(() => expect(dialog).not.toBeVisible());
  });

  test('loads existing recommendations on mount and renders cards', async () => {
    setUserEmail('test@example.com');
    jest.spyOn(StudentService, 'getUserConsent').mockResolvedValue(true);
    jest.spyOn(StudentService, 'getRecommendationsByEmail').mockResolvedValue([
      {
        course_name: 'Intro to AI',
        domains: 'Artificial Intelligence',
        description: 'Learn AI basics',
        skills: ['ML', 'DL'],
      },
    ]);

    render(<CourseRecommendationForm />);

    // Card title
    expect(await screen.findByText('Intro to AI')).toBeInTheDocument();
    // Domain text
    expect(screen.getByText(/Artificial Intelligence/)).toBeInTheDocument();
    // Skills badges
    expect(screen.getByText(/ML/)).toBeInTheDocument();
    expect(screen.getByText(/DL/)).toBeInTheDocument();
    // Recommended badge
    expect(screen.getByText(/Recommended/)).toBeInTheDocument();
  });

  test('submit calls AcademicProfile and updates recommendations', async () => {
    setUserEmail('test@example.com');
    jest.spyOn(StudentService, 'getUserConsent').mockResolvedValue(true);
    jest.spyOn(StudentService, 'getRecommendationsByEmail').mockResolvedValue([]);

    const academicSpy = jest
      .spyOn(StudentService, 'AcademicProfile')
      .mockResolvedValue([
        {
          course_name: 'Data Science 101',
          domains: 'Data Science',
          description: 'Basics of DS',
          skills: ['Python', 'Pandas'],
        },
      ]);

    render(<CourseRecommendationForm />);

    // Fill inputs
    fireEvent.change(screen.getByLabelText('Highest Degree'), { target: { value: 'Bachelors' } });
    fireEvent.change(screen.getByLabelText('Interests (comma separated)'), { target: { value: 'data, ai' } });
    fireEvent.change(screen.getByLabelText('Certifications (comma separated)'), { target: { value: 'aws, gcp' } });

    // Click submit
    fireEvent.click(screen.getByRole('button', { name: /Get Recommendation/i }));

    await waitFor(() => expect(academicSpy).toHaveBeenCalled());

    // New recommendation appears
    expect(await screen.findByText('Data Science 101')).toBeInTheDocument();
    expect(screen.getByText(/Data Science/)).toBeInTheDocument();
    expect(screen.getByText(/Python/)).toBeInTheDocument();
    expect(screen.getByText(/Pandas/)).toBeInTheDocument();
  });

  test('disables button and shows loading while requesting', async () => {
    setUserEmail('test@example.com');
    jest.spyOn(StudentService, 'getUserConsent').mockResolvedValue(true);
    jest.spyOn(StudentService, 'getRecommendationsByEmail').mockResolvedValue([]);

    let resolveCall;
    const pendingPromise = new Promise((res) => { resolveCall = res; });
    jest.spyOn(StudentService, 'AcademicProfile').mockReturnValue(pendingPromise);

    render(<CourseRecommendationForm />);

    const button = screen.getByRole('button', { name: /Get Recommendation/i });
    fireEvent.click(button);

    // Button switches to Loading... and disabled
    expect(screen.getByRole('button', { name: /Loading.../i })).toBeDisabled();

    // Resolve the pending call
    resolveCall([]);

    // Button returns to normal label
    await waitFor(() => expect(screen.getByRole('button', { name: /Get Recommendation/i })).toBeEnabled());
  });
});
