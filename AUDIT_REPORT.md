# Open-Source Compliance & Code Audit Report

**Repository:** `graphql-jena`  
**Date of Last Audit:** `2025-03-21`  
**Reviewed By:** `Kainos Software`
<!-- SPDX-License-Identifier: OGL-UK-3.0 -->
---

## Overview
As part of NDTP’s commitment to open-source compliance and security best practices, this repository has undergone
a comprehensive audit using FOSSology and Copyleaks to verify:

- All third-party components are properly licensed and attributed.
- No proprietary or restricted-license code has been included.
- No unintentional code duplication from external sources.
- All code follows NDTP’s dual-license model (Apache 2.0 for code, OGL-UK-3.0 for documentation).

---

## Tools Used for the Audit

| Tool | Purpose | Scan Date |
|------|---------|----|
| FOSSology | Open-source license compliance scanner | `2025-03-21` |
| Copyleaks | AI-driven plagiarism and duplicate code detection | `2025-03-21` |
| Manual Review | Compliance team manually reviewed flagged files |  `2025-03-21` |

---

## License Compliance Check (FOSSology)
Issues Identified:

FSSology scans identified potential GPL2.0 license matches but a manual review confirmed these to be false positives

Action Taken:
- None required

All required attributions have been added to [NOTICE.md](NOTICE.md)


## Duplicate Code and Attribution Check (Copyleaks)

| Scanned Files | Plagiarism Risk Detected? | Source Match                                                                                  | Resolution |
|--------------|--------------------|-----------------------------------------------------------------------------------------------|--------|
| `GraphQLRequest.java ` | `Yes`              | [source](https://github.com/Unleash/unleash-docker/blob/main/wait-for)                        | Matched parts of code on GitHub   |


Issues Identified and Resolutions:
- None required

All unintentional code reuse has been resolved or attributed correctly.

---

## Final Compliance Status

After running FOSSology and Copyleaks scans, this repository is:

- Fully Compliant
- Necessary Actions Taken



Next Steps:
- None required

Maintained by the National Digital Twin Programme.
