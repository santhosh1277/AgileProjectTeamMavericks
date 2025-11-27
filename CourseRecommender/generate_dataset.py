import json
from pathlib import Path


programs = [
    "MSc in Data Science",
    "MSc in Machine Learning",
    "MSc in Cybersecurity",
    "MSc in Software Engineering",
    "MSc in Business Analytics",
    "MSc in Finance",
    "MSc in UX/UI and Digital Experience",
    "MSc in Robotics",
    "MSc in Bioinformatics",
    "MSc in Public Health"
]

program_map = {
    "MSc in Data Science": {"skills":["Python","SQL","Data Analysis","Machine Learning"], "domains":["Data Science"], "backgrounds":["Computer Science","Statistics"], "careers":["Data Scientist"]},
    "MSc in Machine Learning": {"skills":["Python","TensorFlow","PyTorch","Deep Learning"], "domains":["Machine Learning"], "backgrounds":["Computer Science","Mathematics"], "careers":["Machine Learning Engineer"]},
    "MSc in Cybersecurity": {"skills":["Python","Network Security","Cryptography"], "domains":["Cybersecurity"], "backgrounds":["Computer Science","IT"], "careers":["Cybersecurity Analyst"]},
    "MSc in Software Engineering": {"skills":["Python","Software Development","DevOps"], "domains":["Software Engineering"], "backgrounds":["Computer Science"], "careers":["Software Developer"]},
    "MSc in Business Analytics": {"skills":["Python","SQL","Data Analysis"], "domains":["Analytics"], "backgrounds":["Business Administration","Statistics"], "careers":["Business Analyst"]},
    "MSc in Finance": {"skills":["Financial Modeling","Excel","Risk Analysis"], "domains":["Finance"], "backgrounds":["Finance","Economics"], "careers":["Financial Analyst"]},
    "MSc in UX/UI and Digital Experience": {"skills":["UX Research","UI Design","Prototyping"], "domains":["HCI"], "backgrounds":["Design","Computer Science"], "careers":["UX Designer"]},
    "MSc in Robotics": {"skills":["Python","Robotics Control","ROS"], "domains":["Robotics"], "backgrounds":["Mechanical Engineering","Computer Science"], "careers":["Robotics Engineer"]},
    "MSc in Bioinformatics": {"skills":["Python","Bioinformatics Tools","Genomics Analysis"], "domains":["Bioinformatics"], "backgrounds":["Biology","Computer Science"], "careers":["Bioinformatics Scientist"]},
    "MSc in Public Health": {"skills":["Epidemiology","Data Analysis","Statistics"], "domains":["Public Health"], "backgrounds":["Biology","Nursing"], "careers":["Public Health Researcher"]}
}

def make_description(program, skills, careers):
    return f"{program} provides training in {', '.join(skills)} and prepares students for roles such as {', '.join(careers)}."


entries = []
target = 100  

for _ in range(target):
    program = programs[_ % len(programs)] 
    skills = program_map[program]["skills"]
    domains = program_map[program]["domains"]
    backgrounds = program_map[program]["backgrounds"]
    careers = program_map[program]["careers"]  
    desc = make_description(program, skills, careers)
    
    entry = {
        "course_name": program,
        "description": desc,
        "domains": domains,
        "skills": skills,
        "ideal_backgrounds": backgrounds,
        "career_paths": careers,
        "embedding_text": f"{program}. {desc}. Skills: {', '.join(skills)}. Domains: {', '.join(domains)}."
    }
    entries.append(entry)


out = Path("courses_full_domain_high_quality.json")
with out.open("w", encoding="utf-8") as f:
    json.dump(entries, f, indent=2, ensure_ascii=False)

print(f"Wrote {len(entries)} simplified test courses with relevant careers to {out.resolve()}")
