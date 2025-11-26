# recommend.py
import json
from sentence_transformers import SentenceTransformer, util

# Use a stronger model
model = SentenceTransformer("all-mpnet-base-v2")

def load_courses(path="courses_full_domain_high_quality.json"):
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)

courses = load_courses()

def embed(text):
    return model.encode(text, convert_to_tensor=True)

def infer_domains_from_interests(interests):
    # simple mapping, extend as needed
    m = {
        "ai":["Artificial Intelligence","Machine Learning"],
        "ml":["Machine Learning","Artificial Intelligence"],
        "data":["Data Science","Statistics"],
        "cloud":["Cloud Computing","DevOps"],
        "iot":["Internet of Things","Embedded Systems"],
        "robot":["Robotics"],
        "bio":["Bioinformatics","Biomedical Engineering"],
        "quantum":["Quantum Computing"],
        "cyber":["Cybersecurity"],
        "ux":["Human-Computer Interaction","Design"],
        "finance":["Finance","FinTech"],
        "marketing":["Digital Marketing","Marketing Science"]
    }
    domains = set()
    for it in interests:
        key = it.lower()
        for k, doms in m.items():
            if k in key:
                domains.update(doms)
    return domains

def recommend_courses(background, certifications, interests, top_k=2):
    bg = background.strip()
    certs = [c.strip() for c in certifications if c.strip()]
    ints = [i.strip() for i in interests if i.strip()]

    requested_domains = infer_domains_from_interests(ints)
    # Filter: prefer matching domains and backgrounds
    filtered = courses
    if requested_domains:
        filtered_by_domain = [c for c in courses if any(d in c.get("domains",[]) for d in requested_domains)]
        if filtered_by_domain:
            filtered = filtered_by_domain

    filtered_by_bg = [c for c in filtered if any(bg.lower() in b.lower() for b in c.get("ideal_backgrounds",[]))]
    if filtered_by_bg:
        filtered = filtered_by_bg


    e_bg = embed(bg)
    e_certs = embed(", ".join(certs)) if certs else None
    e_ints = embed(", ".join(ints)) if ints else None

    scored = []
    for c in filtered:
        bg_text = " ".join(c.get("ideal_backgrounds",[]))
        cert_text = " ".join(c.get("skills",[]))
        int_text = " ".join(c.get("domains",[])) + " " + " ".join(c.get("skills",[]))

        e_bg_c = embed(bg_text) if bg_text else None
        e_cert_c = embed(cert_text) if cert_text else None
        e_int_c = embed(int_text) if int_text else None

        s_bg = util.cos_sim(e_bg, e_bg_c).item() if e_bg_c is not None else 0.0
        s_cert = util.cos_sim(e_certs, e_cert_c).item() if (e_certs is not None and e_cert_c is not None) else 0.0
        s_int = util.cos_sim(e_ints, e_int_c).item() if (e_ints is not None and e_int_c is not None) else 0.0

        final_score = 0.5 * s_bg + 0.2 * s_cert + 0.3 * s_int
        scored.append((final_score, c))

    scored.sort(reverse=True, key=lambda x: x[0])
    return scored[:top_k]
