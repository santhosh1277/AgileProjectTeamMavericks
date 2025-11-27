# main.py
from recommend import recommend_courses

degree = input("C: ").strip()
certs = input("Enter certifications (comma-separated): ").strip().split(",")
interests = input("Enter your interests (comma-separated): ").strip().split(",")

certs = [c.strip() for c in certs if c.strip()]
interests = [i.strip() for i in interests if i.strip()]

recs = recommend_courses(degree, certs, interests, top_k=2)

print("\nTop recommended courses:\n")
for score, course in recs:
    print(f"Course: {course['course_name']}")
    print(f"Domains: {', '.join(course.get('domains', []))}")
    print(f"Skills: {', '.join(course.get('skills', []))}")
    print(f"Score: {round(score, 3)}")
    print(f"Description: {course.get('description')}")
    print("-" * 70)
