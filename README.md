<div align="center" style="padding: 10%;">

<img src="app/src/main/ic_launcher-playstore.png" alt="Multiproductos Mascotas logo" width="140"/>

# Multiproductos Mascotas

## Tótem de autopedido para una tienda de productos para mascotas

<p>
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />
  <img src="https://img.shields.io/badge/Stripe-635BFF?style=for-the-badge&logo=stripe&logoColor=white" />
</p>

<p>
  <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" />
  <img src="https://img.shields.io/badge/Material%20Design-757575?style=flat-square&logo=materialdesign&logoColor=white" />
  <img src="https://img.shields.io/badge/minSdk-24-3DDC84?style=flat-square" />
  <img src="https://img.shields.io/badge/targetSdk-34-3DDC84?style=flat-square" />
</p>

</div>


## Descripción

App nativa de Android diseñada como tótem de autopedido. La app muestra el catálogo de la tienda organizado en tres niveles (categoría principal, subcategoría y producto), permite agregar productos al carrito con control de stock y finalizar la compra con pago con tarjeta o en efectivo.

Está pensada para un tótem:

- Pantalla completa con modo inmersivo (se ocultan las barras del sistema).
- Mensajes de confirmación y error visibles sobre la pantalla actual.
- Regreso automático al inicio tras 60 segundos sin interacción, con vaciado del carrito para el siguiente cliente.

## Características

|Característica | Descripción |
|---|---|
|**Catálogo por categorías** | Categoría principal → subcategoría → productos, cargados desde Firebase Realtime Database. |
|**Control de stock** | Solo se muestran productos con stock disponible y el detalle valida la cantidad antes de agregar. |
|**Detalle de producto** | Imagen, descripción, precio, marca y selector de cantidad. |
|**Carrito persistente** | Se guarda en el dispositivo (SharedPreferences), con aumento, disminución y eliminación de ítems y cálculo del total con impuesto. |
|**Pago con tarjeta** | Hoja de pago de Stripe (PaymentSheet) para tarjetas. |
|**Pago en efectivo** | Pantalla de caja con factura, que descuenta el stock al confirmar. |
|**Datos del cliente** | Formularios de cédula, nombre, dirección, teléfono y correo antes del pago. |
|**Feedback en pantalla** | Mensajes de éxito (se ocultan solos) y de error (se quedan hasta que el cliente los toca). |
|**Inactividad** | Vuelve al inicio y vacía el carrito tras 60 segundos sin tocar la pantalla. El flujo de pago está excluido de este regreso automático. |

> Las credenciales y la configuración privada no están incluidas en este repositorio; ver [Configuración](#configuración).

## Arquitectura

Las pantallas heredan de `BaseActivity`, que centraliza el feedback, la inactividad y el modo inmersivo. La lógica del carrito vive en `ManagmentCart`, que persiste los datos con `TinyDB`. Firebase y Stripe son los servicios externos.

```mermaid
graph TD
    A[Intro_Activity] --> M[MainActivity]
    M --> LC[ListCategoryActivity]
    LC --> LP[ListProductsActivity]
    LP --> D[DetailActivity]
    D --> C[CartActivity]
    C --> PM[PaymentMethodActivity_1 / _2]
    PM --> CARD[CardActivity]
    PM --> CASH[CashActivity]
    CASH --> F[FactActivity]
    F --> CO[CorectActivity / CorectActivity2]

    B[BaseActivity<br/>feedback · inactividad · inmersivo]
    A -. hereda .-> B
    M -. hereda .-> B
    D -. hereda .-> B
    C -. hereda .-> B
    CARD -. hereda .-> B
    CASH -. hereda .-> B

    subgraph DATOS["💾 Datos locales"]
        CM[ManagmentCart<br/>lógica del carrito]
        T[TinyDB]
        SP[(SharedPreferences)]
        CM --> T --> SP
    end

    D --> CM
    C --> CM

    subgraph EXTERNOS["☁️ Servicios externos"]
        FB[(Firebase Realtime DB<br/>CategoryOne · CategoryTwo · Products)]
        ST[Stripe<br/>PaymentSheet + API]
    end

    M --> FB
    LC --> FB
    LP --> FB
    CASH -- descuenta stock --> FB
    CARD -- descuenta stock --> FB
    CARD --> ST

    classDef external fill:#4A4A4A,stroke:#AAAAAA,color:#E0E0E0,stroke-dasharray: 5 5
```

**Nodos centrales:**

| Clase | Rol |
|---|---|
| `Product` | Modelo de producto, usado por casi todas las pantallas y adaptadores. |
| `TinyDB` | Persistencia local sobre SharedPreferences. |
| `ManagmentCart` | Reglas del carrito: agregar, quitar, cantidades y total. |
| `BaseActivity` | Base de todas las pantallas: feedback, inactividad y modo inmersivo. |

## Estructura del proyecto

```text
app-empre/
├── 📁 app/
│   ├── 📄 build.gradle
│   ├── 📄 google-services.json          # NO incluido (ver Configuración)
│   └── 📁 src/
│       ├── 📁 main/
│       │   ├── 📄 AndroidManifest.xml
│       │   ├── 📁 java/com/emprendimiento/multiproductosmascotas/
│       │   │   ├── 📁 Activity/         # Pantallas (una actividad por pantalla)
│       │   │   ├── 📁 Adapter/          # Adaptadores de RecyclerView
│       │   │   ├── 📁 Domain/           # Modelos: Product, CategoryOne, CategoryTwo
│       │   │   └── 📁 Helper/           # ManagmentCart, TinyDB, ChangeNumberItemsListener
│       │   └── 📁 res/                  # Layouts, drawables, colores, temas, strings
│       ├── 📁 test/                     # Pruebas unitarias
│       └── 📁 androidTest/              # Pruebas instrumentadas
├── 📁 database/
│   └── 📄 ProductosMascotas-placeholders.json  # Esquema bd
├── 📁 docs/
│   └── 📁 screenshots/                  # Capturas de la app
├── 📁 gradle/
│   └── 📄 libs.versions.toml            # Catálogo de versiones
├── 📄 build.gradle
├── 📄 settings.gradle
├── 📄 gradle.properties
└── 📄 README.md
```


## Modelo de datos

Para crear o actualizar la estructura de Firebase Realtime Database, usa como guía el archivo [`database/ProductosMascotas-placeholders.json`](database/ProductosMascotas-placeholders.json). El resumen siguiente solo muestra los nodos y campos principales:

| Nodo | Campos principales |
|---|---|
| `CategoryOne` | `Id`, `Name` |
| `CategoryTwo` | `Id`, `Name`, `CategoryOneId` |
| `Products` | `Id`, `CategoryOneId`, `CategoryTwoId`, `Title`, `Description`, `Price`, `Stock`, `ImagePath`, `Mark`, `BestProduct` |

El `Id` de cada producto coincide con su clave en `Products`, porque el pago descuenta el stock buscando `Products/{Id}`.

Las reglas de Realtime Database necesitan índices para las consultas por categoría:

```json
{
  "rules": {
    "CategoryTwo": { ".indexOn": ["CategoryOneId"] },
    "Products": { ".indexOn": ["CategoryTwoId"] }
  }
}
```

## Stack tecnológico

| Categoría | Tecnología |
|---|---|
| IDE | Android Studio |
| Lenguaje | ![Java](https://img.shields.io/badge/-Java%208-ED8B00?logo=openjdk&logoColor=white) Java 8 |
| Plataforma | ![Android](https://img.shields.io/badge/-Android-3DDC84?logo=android&logoColor=white) Android (minSdk 24, targetSdk 34) |
| Build | ![Gradle](https://img.shields.io/badge/-Gradle-02303A?logo=gradle&logoColor=white) Gradle (wrapper 9.7.1), Android Gradle Plugin 8.5.0 |
| UI | XML, ViewBinding, Material Components, ConstraintLayout, RecyclerView |
| Imágenes | [Glide](https://github.com/bumptech/glide) 4.12.0 |
| Red | [Volley](https://developer.android.com/training/volley) 1.2.1 |
| Backend | ![Firebase](https://img.shields.io/badge/-Firebase-FFCA28?logo=firebase&logoColor=black) Firebase Realtime Database (Firebase Auth inicializado, sin flujo de login) |
| Pagos | ![Stripe](https://img.shields.io/badge/-Stripe-635BFF?logo=stripe&logoColor=white) Stripe Android SDK 17.2.0 (PaymentSheet) |
| Persistencia local | SharedPreferences (TinyDB), Gson 2.9.1 |
| Pruebas | JUnit 4, AndroidX Test, Espresso |


## Requisitos

**Requisitos previos:**

- Android Studio Iguana o superior.
- JDK 17 para ejecutar Gradle.
- Un dispositivo o emulador con Android 7.0 (API 24) o superior.
- Un proyecto de Firebase con Realtime Database y Authentication habilitados.
- Una cuenta de Stripe en modo de pruebas.


## Configuración

1. **Clona el repositorio** y ábrelo en Android Studio.

2. **Agrega tu `google-services.json`**. Descárgalo desde la consola de Firebase (configuración del proyecto → tu app Android) y colócalo en `app/google-services.json`. Este archivo no se incluye en el repositorio.

3. **Configura las claves de Stripe**. El repositorio no incluye claves. En `app/src/main/java/com/emprendimiento/multiproductosmascotas/Activity/CardActivity.java` reemplaza los valores de `SECRET_KEY` y `PUBLISH_KEY` por tus claves de prueba.

   > Este enfoque solo sirve para pruebas. La clave secreta no debe estar en la app.

4. **Carga los datos de ejemplo**. En la consola de Firebase, ve a *Realtime Database → Datos → ⋮ → Importar JSON* y elige [`database/ProductosMascotas-placeholders.json`](database/ProductosMascotas-placeholders.json).

## Screenshots

<div align="center">

| Inicio | Catálogo | Subcatálogo |
|:---:|:---:|:---:|
| <img src="docs/screenshots/inicio.png" width="200" alt="Inicio" /> | <img src="docs/screenshots/catalogo.png" width="200" alt="Catálogo" /> | <img src="docs/screenshots/subcatalogo.png" width="200" alt="Productos" /> |

| Subcatalogo | Detalle | Carrito |
|:---:|:---:|:---:|
| <img src="docs/screenshots/productos.png" width="200" alt="Inicio" /> | <img src="docs/screenshots/detalle.png" width="200" alt="Catálogo" /> | <img src="docs/screenshots/carrito.png" width="200" alt="Productos" /> |

| Pago | Caja | Tarjeta |
|:---:|:---:|:---:|
| <img src="docs/screenshots/pago.png" width="200" alt="Detalle" /> | <img src="docs/screenshots/caja.png" width="200" alt="Carrito" /> | <img src="docs/screenshots/tarjeta.png" width="200" alt="Pago" /> |

| Mensaje final |
|:---:|
| <img src="docs/screenshots/final.png" width="200" alt="Detalle" /> |

</div>


## Información general

- **Nombre de la aplicación:** Multiproductos Mascotas · **Paquete:** `com.emprendimiento.multiproductosmascotas` · **Versión:** `1.0`
- **UI language:** Español
- **Estado:** en desarrollo.
- **Nota de seguridad:** Este es un proyecto demostrativo. Antes de usarlo con cobros reales, implementa el backend de pagos.

---

<div align="center">

Creado con Android Studio y Java por **Pablo Simbaña**

</div>
