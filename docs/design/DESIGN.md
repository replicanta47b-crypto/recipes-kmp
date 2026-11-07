---
name: Warm Culinary Modern
colors:
  surface: '#fff8f6'
  surface-dim: '#e9d6d1'
  surface-bright: '#fff8f6'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#fff1ed'
  surface-container: '#feeae4'
  surface-container-high: '#f8e4df'
  surface-container-highest: '#f2ded9'
  on-surface: '#231916'
  on-surface-variant: '#58423b'
  inverse-surface: '#392e2a'
  inverse-on-surface: '#ffede8'
  outline: '#8b7169'
  outline-variant: '#dfc0b6'
  surface-tint: '#a63b10'
  primary: '#942e02'
  on-primary: '#ffffff'
  primary-container: '#b5451b'
  on-primary-container: '#ffe4dc'
  inverse-primary: '#ffb59d'
  secondary: '#4b662a'
  on-secondary: '#ffffff'
  secondary-container: '#cceea2'
  on-secondary-container: '#516d2f'
  tertiary: '#755b00'
  on-tertiary: '#ffffff'
  tertiary-container: '#cba741'
  on-tertiary-container: '#503d00'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffdbd0'
  primary-fixed-dim: '#ffb59d'
  on-primary-fixed: '#390c00'
  on-primary-fixed-variant: '#832600'
  secondary-fixed: '#cceea2'
  secondary-fixed-dim: '#b0d188'
  on-secondary-fixed: '#102000'
  on-secondary-fixed-variant: '#344e14'
  tertiary-fixed: '#ffe08f'
  tertiary-fixed-dim: '#e8c259'
  on-tertiary-fixed: '#241a00'
  on-tertiary-fixed-variant: '#584400'
  background: '#fff8f6'
  on-background: '#231916'
  surface-variant: '#f2ded9'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.015em
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 34px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: 0.005em
  body-lg:
    fontFamily: Inter
    fontSize: 17px
    fontWeight: '400'
    lineHeight: 26px
  body-md:
    fontFamily: Inter
    fontSize: 15px
    fontWeight: '400'
    lineHeight: 22px
  body-sm:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1rem
  gutter-tablet: 1.5rem
  margin-tablet: 2rem
  space-xxs: 0.25rem
  space-xs: 0.5rem
  space-sm: 0.75rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
  space-2xl: 3rem
---

## Brand & Style

The design system establishes a warm, tactile, and appetizing atmosphere tailored for everyday home cooking and culinary exploration. Combining the structural discipline of Material Design 3 with an inviting, organic warmth, the interface balances utility with emotional comfort.

### Personality & Emotional Resonance
- **Warm & Hospitable:** Elicits the feeling of a sunlit kitchen, aromatic spices, and rustic ceramics without succumbing to retro kitsch.
- **Supportive & Clear:** Cooking can be chaotic; the UI remains uncluttered, highly legible from counter-top viewing distances, and reassuringly structured.
- **Tactile & Modern:** Uses soft, approachable geometric curves, pill containers, and gentle surface contrasts that invite intuitive touch interaction.

### Design Movement
**Modern Material 3 with Organic Tactility.** The visual language relies on tonal surface layering, spacious layout paddings, deliberate container hierarchies, and soft pill geometry rather than hard industrial borders.

## Colors

The palette draws directly from pantry staples and natural culinary elements: sun-dried terracotta clay, garden herbs, and saffron pollen against an unbleached linen background.

### Semantic Palette Tiers
- **Primary (`#B5451B` - Terracotta):** Anchors brand-critical calls to action, cooking timer states, key step counters, and primary navigation active markers.
  - *Primary Container (`#FFDBCF`):* Subtle emphasis for prominent actions like "Start Cooking" or highlighted nutritional values.
  - *On Primary Container (`#3A0B00`):* High-contrast dark earthen red for container text.
- **Secondary (`#4F6B2E` - Olive Herb):** Balances primary heat with herbal calm. Reserved for dietary tags (vegan, vegetarian, gluten-free), ingredient checklist confirmations, and pantry tracking indicators.
  - *Secondary Container (`#D2ECAE`):* Soft green container for completed steps and success states.
- **Tertiary (`#8B6D00` - Warm Saffron):** Highlights cooking tips, chef notes, prep times, and rating badges.
  - *Tertiary Container (`#FFE17D`):* Light saffron card headers and tip backgrounds.
- **Surfaces & Neutrals:**
  - *Surface / Background (`#FFF8F5`):* Warm off-white canvas preventing clinical glare in kitchen environments.
  - *Surface Container / Cards (`#F5ECE7`):* Subtle warm tint separating content blocks without harsh boundaries.
  - *Text Primary / On Surface (`#231916`):* Deep espresso-charcoal providing high legibility.
  - *Text Secondary (`#53433E`):* Muted roasted-coffee tone for metadata, units, and timestamps.
  - *Outline / Divider (`#85736E`):* Used at low opacity (12%–20%) for non-intrusive structural divisions.

## Typography

Inter serves as the sole typographic engine, providing complete structural parity between Latin and Cyrillic script requirements for dual Ukrainian-English execution.

### Typographic Hierarchy & Reading Dynamics
- **Headlines & Titles:** Set with tighter tracking and bold weights (`600` to `700`) to anchor recipe names, step numbers, and section headers firmly against warm backgrounds.
- **Instructional Body:** Given generous leading (`17px/26px` for `body-lg`) to facilitate effortless glanceability while cooking with hands occupied or device resting on a stand.
- **Labels & Metrics:** Slightly tracked positive labels (`label-md` and `label-sm`) for cook times, servings, and temperature readouts to maintain crispness at compact sizes.

## Layout & Spacing

A fluid grid system built around an 8px base rhythm provides comfortable finger-friendly hit targets and open culinary whitespace.

### Layout Philosophy & Structure
- **Mobile First (4-column grid):** Outer margins locked to `1rem` (16px) with `1rem` gutters. Recipe feeds flow in single-column stacks or alternating two-column cards.
- **Tablet & Countertop Display (8-column grid):** Outer margins expand to `2rem` (32px), with `1.5rem` gutters. Accommodates two-pane views: ingredient checklist pinned left, interactive steps scrolling right.
- **Rhythm & Glanceability:** Recipe steps leverage `space-lg` to `space-xl` separation to ensure steps never blur together visually when referencing the screen mid-prep.

## Elevation & Depth

This design system favors tonal hierarchy and warm ambient diffusion over stark, cold drop shadows.

### Surface Tonal Hierarchy
- **Level 0 (Base Canvas):** `#FFF8F5` serves as the canvas for scrollable content and overall page backdrops.
- **Level 1 (Card & Content Container):** Elevated via `#F5ECE7` surface fill with no shadow. Creates quiet grouping for ingredient clusters, reviews, and nutritional tables.
- **Level 2 (Floating Action & Interactive Trays):** Pinned bottom sheets, sticky cooking mode toolbars, and primary CTAs leverage soft, warm-tinted ambient shadows:
  - Offset: `0px 4px 16px`
  - Blur Tint: `rgba(83, 67, 62, 0.08)` (roasted coffee tint instead of pure neutral gray).
- **Level 3 (Modal & Full Screen Drawer):** High ambient blur `0px 12px 32px rgba(35, 25, 22, 0.14)` combined with 1px hairline border in `rgba(133, 115, 110, 0.15)`.

## Shapes

The shape system adopts a friendly, tactile curvature that echoes rounded kitchenware and smooth ceramic forms.

### Corner Curvature Rules
- **Base Components (`rounded` / 8px):** Checkboxes, form text inputs, small badges, and nutritional stat containers.
- **Cards & Sheets (`rounded-lg` / 16px):** Recipe preview cards, step containers, cooking timers, and modal dialogs.
- **Feature Cards & Hero Modules (`rounded-xl` / 24px):** Recipe hero images, featured collection covers, and drawer sheets.
- **Pill Elements (Full Radius / 9999px):** All interactive buttons, chips, filter tags, and quantity steppers adhere to full pill styling consistent with Material Design 3.

## Components

### Buttons
- **Filled Primary:** Full pill radius (`rounded-full`), height 48px on mobile, fill `#B5451B`, text `#FFFFFF` in `label-lg`. Pressed state transitions to a deeper terracotta overlay (`#993812`).
- **Tonal Container:** Fill `#FFDBCF`, text `#3A0B00`. Used for secondary actions within the cooking flow (e.g., "Add to Grocery List").
- **Outlined / Ghost:** Transparent fill, 1.5px border `#85736E` at 30% opacity, text `#231916`.

### Chips & Filter Tags
- **Filter Chips:** Height 36px, full pill shape. Inactive: `#F5ECE7` container with `#53433E` label. Active: `#D2ECAE` (Secondary Container) with `#4F6B2E` text and a leading checkmark icon.
- **Ingredient Measurement Badges:** Pill-shaped, tertiary container `#FFE17D` with `#5A4500` text for prep times and temperatures.

### Cards
- **Recipe Feed Card:** Surface container fill `#F5ECE7`, radius `16px`, zero border. Hero image sits with matching top radius or inner inset. Contains recipe title (`headline-sm`), prep time chip, difficulty badge, and bookmark icon at top right.
- **Step Card:** Clean white or elevated card with large step counter in `#B5451B` (`headline-md`), step instructions in `body-lg`, and interactive checkboxes.

### Checkboxes & List Items
- **Ingredient Checklist:** Row items with a minimum height of 48px for effortless kitchen tapping. Checkbox is 22px with `4px` corner radius; when checked, transitions to `#4F6B2E` fill with white checkmark and strikes through text in `#85736E`.
- **Quantity Steppers:** Integrated pill module (`-`, count, `+`) in `#F5ECE7` for adjusting serving sizes dynamically.

### Input Fields
- **Search & Recipe Notes:** Filled MD3 style with `#F5ECE7` background, radius `12px` or pill for search bars. Placeholder in `#85736E`. Active focus shows a 2px active indicator ring in `#B5451B`.

### Recipe-Specific Additions
- **Cooking Mode Step Focus:** Fullscreen single-step runner with large timer pill widget (`#B5451B` background, white countdown text) and high-contrast instruction text visible from 1 meter away.
- **Dietary Badges:** Compact pill chips featuring `#4F6B2E` text on `#D2ECAE` for rapid dietary identification.