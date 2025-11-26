from flask import Flask, request, jsonify
from recommend import recommend_courses

app = Flask(__name__)

def format_recommendations(recs):
    formatted = []
    for score, course in recs:
        formatted.append({
            "course_name": course["course_name"],
            "domains": course.get("domains", []),
            "skills": course.get("skills", []),
            "score": round(score, 3),
            "description": course.get("description", "")
        })
    return formatted

@app.route("/recommend", methods=["POST"])
def recommend_endpoint():
    try:
        data = request.json

        degree = data.get("degree")
        certs = data.get("certifications", [])
        interests = data.get("interests", [])

        # Validate inputs
        if not degree or not certs or not interests:
            return jsonify({"error": "Missing degree, certifications, or interests"}), 400

        # Strip whitespace
        certs = [c.strip() for c in certs if c.strip()]
        interests = [i.strip() for i in interests if i.strip()]

        # Call your existing function
        recs = recommend_courses(degree, certs, interests, top_k=2) 
        result = format_recommendations(recs)

        return jsonify({"recommendations": result})

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
