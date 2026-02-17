# 🚗 CarHealthCheck

Web application supporting the evaluation of a used car before purchase.

CarHealthCheck is an engineering thesis project focused on reducing uncertainty when buying a used vehicle. The system aggregates inspection data, VIN history information and technical checklist answers into a synthetic risk assessment using a deterministic rule-based scoring mechanism.

---

## 🎯 Goal

The goal of the project was to design and implement a web application that:

- structures the vehicle inspection process,
- aggregates data from multiple sources (VIN, visual inspection, technical checklist),
- calculates a risk score,
- generates a clear and interpretable report (PDF),
- supports users without specialist automotive knowledge.

> The system does **not replace professional diagnostics** — it supports preliminary decision-making.

---

## 🧩 Features

- User registration and login
- Vehicle management (add, save, review history)
- VIN report analysis (incl. missing data handling)
- Visual inspection module (paint thickness + visual differences)
- Technical checklist (risk bands + weights)
- Risk score + classification (LOW / MEDIUM / HIGH)
- PDF report generation

---

## 🏗 Architecture

The application follows the **Model–View–Controller (MVC)** pattern.

**Technology stack:**

- **Backend:** Java + Spring Boot
- **Frontend:** Thymeleaf + HTML + CSS
- **Database:** PostgreSQL
- **VCS:** Git

**Layers:**

- **Controller** — handles HTTP requests
- **Service** — business logic (scoring engine)
- **Repository/DAO** — database access
- **View** — Thymeleaf templates (UI)

---

## 🧠 Scoring Mechanism (Rule-based Expert System)

The scoring engine is deterministic and interpretable:

- predictable behavior (same input → same output)
- transparent penalties and thresholds
- easy extension with new rules

### 🔢 High-level algorithm

1. Determine baseline score `S0` (default: `60` if missing)
2. Compute penalty sum `P`:
    - VIN analysis
    - paint inspection
    - technical checklist
3. Final score:

\[
S = clamp(S0 - P, 0, 100)
\]

### 🧾 VIN analysis

- Missing VIN → penalty
- Risk flags (e.g. theft / total loss / odometer mismatch / scrapped / not roadworthy) → high penalties

### 🎨 Paint inspection

- Missing paint data or missing thickness measurements → penalty
- Uses median vs extreme values to detect:
    - possible filler use
    - repainting
- Counts visual differences between parts (`diffCount`) and applies additional penalties

### ✅ Technical checklist

Each answer belongs to a risk band:

- `GREEN` → 0
- `YELLOW` → 2
- `RED` → 6

Penalties are multiplied by per-question weights (critical / high / medium / default).

### 🚦 Risk level thresholds

- **LOW** → `S ≥ 75`
- **MEDIUM** → `45 ≤ S < 75`
- **HIGH** → `S < 45`

---

## 🧪 Testing

Manual functional tests covered:

- form flow and validation
- scoring correctness and consistency
- handling incomplete input data
- correct presentation of results
- PDF generation consistency with UI output

---

## 📱 Usability

The UI is designed to:

- guide the user step-by-step through inspection stages,
- minimize cognitive load,
- show not only the final score, but also *reasons* behind the assessment,
- work on mobile devices during real vehicle inspection.

---

## 🚀 Future Improvements

Possible extensions:

- integration with additional external data sources
- expanding the rule base
- estimating financial impact (repair cost projection)
- optional ML-based enhancement (while maintaining interpretability)
- improved UX and mobile ergonomics

---

## 📘 Thesis Context

This project was developed as part of an Engineering Thesis:

**"Aplikacja webowa wspierająca zakup samochodu"**  
University of Silesia — Faculty of Science and Technology (Engineering Computer Science)

---

## 📄 License

Add your license here (e.g. MIT) or remove this section if not applicable.
