# ShopNow - A Comprehensive Shopping App

ShopNow is a robust and user-friendly shopping application built with Kotlin, utilizing modern Android development techniques. The app integrates seamlessly with Firebase for authentication and data storage, and Razorpay for secure payments. This project showcases the implementation of MVVM architecture, coroutines, and the navigation component.
## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Screenshots](#screenshots)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Setup](#setup)
- [Usage](#usage)
  - [Authentication](#authentication)
  - [Shopping](#shopping)
  - [Search Product](#search-product)
  - [Checkout Process](#checkout-process)
  - [Profile Management](#profile-management)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Features

- **Welcome Screen**: A welcoming interface for users upon launching the app.
- **Authentication**:
  - Login with email and password.
  - Register a new account.
  - Forgot password functionality.
- **Shopping Activity**:
  - **Home**: At the top there is Auto image slider for showing important banners,displays product categories, best deals, and bestsellers and many more.
  - **Explore**: Browse all available products across all categories.
  - **Cart**: View and manage items added to the cart.
  - **Profile**: Manage user profile, addresses, and log out.
- **Product Details**: Detailed information about each product with an auto image slider for showing different pictures of products, with options to add to cart.
- **Search Product**: Real-time search functionality to find products quickly and efficiently.
- **Cart Management**: Increase or decrease product quantities in the cart and also can delete the item from the cart
- **Checkout Process**:
  - Billing and address selection.
  - Payment integration with Razorpay.
- **Order Confirmation**: Displays order confirmation with an order number.
- **Order Details**: View order status, address, and product details.
- **Dependency Injection**: Used Dagger Hilt for efficient dependency injection.
- **Profile Management**: Update profile image, name, and manage addresses.
- **Logout**: Securely log out of the app.

## Tech Stack

- **Kotlin**: The main programming language used.
- **Firebase**:
  - Firebase Authentication
  - Firestore
  - Firebase Storage
  - Cloud Firestore
- **MVVM Architecture**: To separate concerns and organize code.
- **ViewModel**: To manage UI-related data lifecycle consciously.
- **Coroutines**: For asynchronous programming.
- **Navigation Component**: For managing in-app navigation.
- **Dagger Hilt**: For dependency injection.
- **Razorpay**: For handling payments.

## Screenshots
#### Login and Register Screen
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/Manishshakya6614/Shop_Now/assets/98381672/b4baa2fa-de76-4341-b642-5612e6a327f8" alt="Headlines Screen" width="400">
      <br>
    </td>
    <td align="center">
      <img src="https://github.com/Manishshakya6614/Shop_Now/assets/98381672/6dff1de3-6774-47ca-a52e-4c5a59d1e819" alt="Favourites Screen" width="400">
      <br>
    </td>
  </tr>
</table>

#### Home Screen
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/Manishshakya6614/Shop_Now/assets/98381672/7f138755-3594-499c-8829-3e3ab33cad88" alt="Headlines Screen" width="400">
      <br>
    </td>
    <td align="center">
      <img src="https://github.com/Manishshakya6614/Shop_Now/assets/98381672/68611421-6a01-4aad-9b04-56ba9f5aeca2" alt="Favourites Screen" width="400">
      <br>
    </td>
  </tr>
</table>

#### Product Detail Screen & Explore Screen
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/Manishshakya6614/Shop_Now/assets/98381672/3309535e-36b7-4436-bc51-c9f06d2e147a" alt="Headlines Screen" width="400">
      <br>
    </td>
    <td align="center">
      <img src="https://github.com/Manishshakya6614/Shop_Now/assets/98381672/47d9dbed-0a4b-4a25-b96f-9b3efc67e9fa" alt="Favourites Screen" width="400">
      <br>
    </td>
  </tr>
</table>

#### Cart Screen & Checkout Screen
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/Manishshakya6614/Shop_Now/assets/98381672/98f7aec3-76a8-409f-b9a0-c807a9798136" alt="Headlines Screen" width="400">
      <br>
    </td>
    <td align="center">
      <img src="https://github.com/Manishshakya6614/Shop_Now/assets/98381672/a285717f-9434-4046-a6e0-8eee8ca0a0a2" alt="Favourites Screen" width="400">
      <br>
    </td>
  </tr>
</table>

#### Profile
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/Manishshakya6614/Shop_Now/assets/98381672/4be515d1-08ad-4429-999b-eb9c7e1f2b93" alt="Headlines Screen" width="400">
      <br>
    </td>
  </tr>
</table>

## Usage

### Authentication

- **Register**: Create a new account by providing an email and password.
- **Login**: Access the app with your credentials.
- **Forgot Password**: Reset your password if forgotten.

### Shopping

- **Home Screen**: Browse product categories, view best deals, and bestsellers.
- **Explore**: Discover all products available in the store.
- **Search**: Use the search bar to find products. The search is real-time and fetches data from Firestore based on the entered text.

### Search Product

- **Real-Time Search**: The search functionality allows users to find products by typing in the search bar.
- **Firebase Firestore**: Fetches data from Firestore collections based on the entered text.
- **Dynamic Updates**: The search results are updated in real-time as the user types, providing a seamless search experience.
- **Progress Indicator**: A progress bar is displayed while the search results are being fetched.

### Checkout Process

- **Cart**: Review items added to the cart, modify quantities, and remove items.
- **Billing**: Select an address for delivery.
- **Payment**: Complete the payment process using Razorpay.
- **Order Confirmation**: Receive an order confirmation with a unique order number.
- **Order Details**: Check order status, delivery address, and detailed product information.

  ### Profile Management

- **View Profile**: See your profile information.
- **Edit Profile**: Update your profile picture and name.
- **Manage Addresses**: Add or edit your delivery addresses or delete your saved address.
- **Logout**: Securely log out from your account.

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit them (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

## Acknowledgements

- **Firebase**: For providing the backend services.
- **Razorpay**: For enabling secure payment processing.
- **Dagger Hilt**: For simplifying dependency injection.
- **Google**: For the Android Jetpack libraries.
- **Open-source Community**: For various libraries and tools that made development easier.



