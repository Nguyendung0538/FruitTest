---
name: Orchard Point of Sale
colors:
  surface: '#faf8ff'
  surface-dim: '#d2d9f4'
  surface-bright: '#faf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3ff'
  surface-container: '#eaedff'
  surface-container-high: '#e2e7ff'
  surface-container-highest: '#dae2fd'
  on-surface: '#131b2e'
  on-surface-variant: '#3e4a3d'
  inverse-surface: '#283044'
  inverse-on-surface: '#eef0ff'
  outline: '#6e7b6c'
  outline-variant: '#bdcaba'
  surface-tint: '#006e2d'
  primary: '#006b2c'
  on-primary: '#ffffff'
  primary-container: '#00873a'
  on-primary-container: '#f7fff2'
  inverse-primary: '#62df7d'
  secondary: '#855300'
  on-secondary: '#ffffff'
  secondary-container: '#fea619'
  on-secondary-container: '#684000'
  tertiary: '#a33900'
  on-tertiary: '#ffffff'
  tertiary-container: '#cc4900'
  on-tertiary-container: '#fffbff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#7ffc97'
  primary-fixed-dim: '#62df7d'
  on-primary-fixed: '#002109'
  on-primary-fixed-variant: '#005320'
  secondary-fixed: '#ffddb8'
  secondary-fixed-dim: '#ffb95f'
  on-secondary-fixed: '#2a1700'
  on-secondary-fixed-variant: '#653e00'
  tertiary-fixed: '#ffdbce'
  tertiary-fixed-dim: '#ffb599'
  on-tertiary-fixed: '#370e00'
  on-tertiary-fixed-variant: '#7f2b00'
  background: '#faf8ff'
  on-background: '#131b2e'
  surface-variant: '#dae2fd'
typography:
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 32px
  headline-md:
    fontFamily: Inter
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  headline-sm:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  title-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 22px
  title-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '600'
    lineHeight: 18px
    letterSpacing: 0.1px
  label-md:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.4px
  label-sm:
    fontFamily: Inter
    fontSize: 10px
    fontWeight: '700'
    lineHeight: 12px
    letterSpacing: 0.5px
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
  margin-tablet: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

The design system is engineered for agile, on-the-floor produce inventory and retail operations. The personality combines market-fresh vibrancy with utility-first operational clarity. It targets boutique grocers, market managers, and fruit stall operators who require rapid single-thumb navigation, instantaneous status recognition, and readable batch metrics under varying retail lighting conditions.

The visual aesthetic balances Modern Material 3 structural foundations with organic, crisp tactile surfaces. Crisp white operational cards rest against a soft, cool canvas to minimize visual fatigue during long inventory intake sessions. Vivid fruit-inspired accents provide unmistakable optical cues for ripeness tracking, perishable expiration windows, and fulfillment workflows without descending into visual clutter.

## Colors

The palette establishes an immediate botanical association centered on farm freshness and urgent inventory monitoring:

- **Primary (`#16a34a`, interactive light `#22c55e`):** Represents healthy stock, active states, prime metrics, and primary confirmation calls to action.
- **Secondary (`#f59e0b`):** Delivers a warm citrus tone used for alerts, impending batch expirations (3–5 days), pending incoming transfers, and auxiliary highlights.
- **Tertiary (`#ea580c`):** Serves as a heightened amber warning for critical shelf-life warnings (24–48 hours) and priority review states.
- **Error / Danger (`#dc2626`):** Dedicated to expired stock, spoiled write-offs, rejected orders, and destructive modal confirmations.
- **Neutral Core (`#0f172a`):** Deep slate providing high-contrast readability across labels, prices, and batch IDs on bright screens.
- **Canvas & Containers:** Base canvas operates on `#f8fafc` (slate-50) paired with pure `#ffffff` cards for content differentiation. Muted labels and dividing lines leverage `#64748b` (slate-500) and `#e2e8f0` (slate-200).

## Typography

The type hierarchy utilizes **Inter** across all typographic roles to preserve high legibility on mobile screens, especially for numeric prices, stock SKUs, and inventory weights.

- Use tabular numeric alignment for stock levels, monetary figures, and weights to prevent layout shift during counter adjustments.
- Headings (`headline-lg`, `headline-md`) are set with tight tracking (`-0.02em`) to maintain punchy mobile app bar headers.
- Badges and status indicators rely on `label-sm` in full uppercase or crisp title case to maximize rapid identification at arm's length.

## Layout & Spacing

The layout is built for hand-held mobile devices with a fluid column system adhering to standard 4-column smartphone grids and 8-column tablet layouts.

- **Margins & Gutters:** Mobile screens enforce a 16px (`1rem`) lateral canvas margin and 16px column gutters. Tablet screens expand to a 24px (`1.5rem`) margin.
- **Rhythm:** Spacing follows a strict 4px/8px modular base scale. Interactive components maintain a minimum hit target of 48px × 48px to allow easy operation with gloved or wet hands in fresh produce environments.
- **Safe Zones:** Content containers dynamically respect top Android status bars and reserve an 80px bottom safe zone to clear the persistent navigation bar and quick-action triggers.

## Elevation & Depth

Visual hierarchy employs Material 3 tonal layering enriched by subtle, organic ambient shadows rather than stark heavy outlines:

- **Level 0 (Base Canvas):** `#f8fafc` flat background. No shadow.
- **Level 1 (Resting Cards, List Rows):** Pure `#ffffff` surface with `0 1px 3px rgba(15, 23, 42, 0.06), 0 1px 2px rgba(15, 23, 42, 0.04)` and a delicate 1px perimeter border of `#f1f5f9`.
- **Level 2 (Active/Pressed Cards, Bottom Sheets):** `0 4px 6px -1px rgba(15, 23, 42, 0.08), 0 2px 4px -2px rgba(15, 23, 42, 0.04)`.
- **Level 3 (Floating Action Button & Bottom Bar):** `0 10px 15px -3px rgba(22, 163, 74, 0.25), 0 4px 6px -4px rgba(22, 163, 74, 0.15)`. Tinted ambient green dispersion highlights the primary inventory scan action.
- **Level 4 (Dialogs & Critical Action Sheets):** `0 20px 25px -5px rgba(15, 23, 42, 0.12), 0 8px 10px -6px rgba(15, 23, 42, 0.06)` with backdrop blur scrim (`rgba(15, 23, 42, 0.4)`).

## Shapes

The design system incorporates rounded geometry (`roundedness: 2`) to echo the natural, organic forms of fresh fruits while maintaining systematic Material 3 consistency:

- **Standard Containers & Cards:** 16px (`rounded-lg`) border radius, balancing friendly contours with optimized space utilization for compact product information.
- **Input Fields & Search Bars:** 12px to 16px border radius.
- **Buttons, Status Chips & FAB:** Fully rounded pill shapes (`9999px`) provide high tactile feedback, finger-friendly targets, and distinct visual separation from structural rectangular cards.
- **Image Thumbnails:** 12px rounded corners to keep organic produce visuals soft and integrated.

## Components

### Buttons
- **Primary FAB (Scan / Add Produce):** 56px circular or extended pill format. Background `#16a34a`, icon/text `#ffffff`. Hover/Pressed state transitions to `#15803d` with Level 3 green ambient shadow.
- **Standard Primary:** 48px height, pill-shaped, `#16a34a` solid background, bold center-aligned label in `#ffffff`.
- **Secondary / Tonal:** `#f0fdf4` background with `#16a34a` typography and iconography.
- **Destructive:** 48px height, `#fef2f2` background with `#dc2626` text; solid `#dc2626` fill for final confirmation dialogs.

### Status Chips (Badges)
Compact pills (24px height, horizontal padding 8px) with `label-sm` font weight:
- **Fresh:** Background `#dcfce7`, text `#15803d`, leading icon dot.
- **Expiring Soon:** Background `#fef3c7`, text `#b45309`.
- **Expired:** Background `#fee2e2`, text `#b91c1c`.
- **Pending:** Background `#f1f5f9`, text `#475569`.
- **Confirmed:** Background `#e0e7ff`, text `#3730a3`.
- **Completed:** Background `#dcfce7`, text `#166534`.

### Product Cards
- Compact 2-column grid or list view card in pure `#ffffff` with 16px corner radius.
- Includes a dedicated image frame (1:1 ratio), bold item title (`title-md`), weight/stock unit (`body-sm` in slate-500), prominent unit price (`title-lg` in slate-900), and a floating top-right status chip.
- Includes quick inline stock increment/decrement steppers (`+` / `-`) built with soft slate-100 pill buttons.

### Bottom Navigation Bar
- Modern floating or docked container with 64px height and Level 3 elevation.
- 4 primary destinations: **Inventory**, **Orders**, **Batches/Expiry**, and **Analytics**.
- Active icons use filled green (`#16a34a`) with a subtle pill-shaped indicator background (`#dcfce7`); inactive icons remain `#64748b`.

### Input Fields & Steppers
- Outlined 52px input containers with 12px rounded corners, resting `#e2e8f0` stroke, transitioning to 2px `#16a34a` focus ring.
- Numeric inputs for kilogram/crate quantities integrate quick preset buttons (+0.5 kg, +1 kg, +5 kg) positioned directly above the keyboard zone.