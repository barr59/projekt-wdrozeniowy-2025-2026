import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Image,
  Alert,
} from "react-native";
import { createClient } from "@supabase/supabase-js";
import { NavigationContainer, NavigationIndependentTree } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";

// Config Supabase
// Tak, API KEY jest w kodzie, można by zrobić tak, by czytał z pliku .env, ale nie jest to projekt faktycznie wdrażany dla jakiejś firmy, więc prostszy jest ten sposób.
const supabaseUrl = "https://vopvwtvyabnlhowstjwc.supabase.co";
const supabaseAnonKey = "sb_publishable_epcTNTKVzDNMk6ZULEDObA_Q8ftNXkM";
const supabase = createClient(supabaseUrl, supabaseAnonKey);

// Ekran logowania
function LoginScreen({ navigation }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async () => {
    if (!email || !password) {
      Alert.alert("Błąd", "Wypełnij oba pola");
      return;
    }

    setLoading(true);
    try {
      const { data, error } = await supabase.auth.signInWithPassword({
        email: email.trim(),
        password,
      });

      if (error) throw error;

      // Pobieranie dane profilu użytkownika
      const { data: profile, error: profileError } = await supabase
        .from("profiles")
        .select("full_name, role, phone")
        .eq("id", data.user.id)
        .single();

      if (profileError && profileError.code !== "PGRST116") {
        // PGRST116 = brak rekordu, powinno być niemożliwe dzięki triggerowi
        throw profileError;
      }

      console.log("Zalogowano:", data.user.email);
      console.log("Dane profilu:", profile);

      Alert.alert("Sukces", `Witaj!`);
      // navigation.navigate('Home') - dodać przekierowanie na stronę główną
    } catch (error) {
      Alert.alert("Błąd logowania", error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <Image
        source={{ uri: "https://via.placeholder.com/120x120.png?text=Hotel" }} //placeholder logo - do zmiany
        style={styles.logo}
      />

      <Text style={styles.title}>Hotel Finder</Text>
      <Text style={styles.subtitle}>Zaloguj się</Text>

      <TextInput
        style={styles.input}
        placeholder="Email"
        placeholderTextColor="#999"
        keyboardType="email-address"
        autoCapitalize="none"
        autoCorrect={false}
        value={email}
        onChangeText={setEmail}
      />

      <TextInput
        style={styles.input}
        placeholder="Hasło"
        placeholderTextColor="#999"
        secureTextEntry
        autoCapitalize="none"
        value={password}
        onChangeText={setPassword}
      />

      <TouchableOpacity
        style={[styles.button, loading && styles.buttonDisabled]}
        onPress={handleLogin}
        disabled={loading}
      >
        <Text style={styles.buttonText}>
          {loading ? "Logowanie..." : "Zaloguj się"}
        </Text>
      </TouchableOpacity>

      <TouchableOpacity onPress={() => navigation.navigate("Registration")}>
        <Text style={styles.link}>Zarejestruj się</Text>
      </TouchableOpacity>
    </View>
  );
}

// Ekran rejestracji
function RegistrationScreen({ navigation }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleRegister = async () => {
    if (!email || !password || !confirmPassword) {
      Alert.alert("Błąd", "Wypełnij wszystkie pola");
      return;
    }

    if (password !== confirmPassword) {
      Alert.alert("Błąd", "Hasła nie są identyczne");
      return;
    }

    if (password.length < 6) {
      Alert.alert("Błąd", "Hasło musi mieć minimum 6 znaków");
      return;
    }

    setLoading(true);
    try {
      const { data, error } = await supabase.auth.signUp({
        email: email.trim(),
        password,
      });

      if (error) throw error;

      Alert.alert(
        "Rejestracja zakończona",
        "Sprawdź swoją skrzynkę email i kliknij w link potwierdzający."
      );

      navigation.navigate("Login");
    } catch (error) {
      Alert.alert("Błąd rejestracji: ", error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Utwórz konto</Text>

      <TextInput
        style={styles.input}
        placeholder="Email"
        placeholderTextColor="#999"
        keyboardType="email-address"
        autoCapitalize="none"
        autoCorrect={false}
        value={email}
        onChangeText={setEmail}
      />

      <TextInput
        style={styles.input}
        placeholder="Hasło (min. 6 znaków)"
        placeholderTextColor="#999"
        secureTextEntry
        autoCapitalize="none"
        value={password}
        onChangeText={setPassword}
      />

      <TextInput
        style={styles.input}
        placeholder="Powtórz hasło"
        placeholderTextColor="#999"
        secureTextEntry
        autoCapitalize="none"
        value={confirmPassword}
        onChangeText={setConfirmPassword}
      />

      <TouchableOpacity
        style={[styles.button, loading && styles.buttonDisabled]}
        onPress={handleRegister}
        disabled={loading}
      >
        <Text style={styles.buttonText}>
          {loading ? "Tworzenie konta..." : "Zarejestruj się"}
        </Text>
      </TouchableOpacity>

      <TouchableOpacity onPress={() => navigation.navigate("Login")}>
        <Text style={styles.link}>Mam już konto - Zaloguj się</Text>
      </TouchableOpacity>
    </View>
  );
}

// Styles - można pozmieniać kolory według palety kolorów apki
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f8fafc",
    padding: 24,
    justifyContent: "center",
  },
  logo: {
    width: 100,
    height: 100,
    alignSelf: "center",
    marginBottom: 32,
    borderRadius: 50,
  },
  title: {
    fontSize: 28,
    fontWeight: "700",
    color: "#1e293b",
    textAlign: "center",
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: "#64748b",
    textAlign: "center",
    marginBottom: 32,
  },
  input: {
    backgroundColor: "white",
    borderRadius: 12,
    padding: 16,
    fontSize: 16,
    borderWidth: 1,
    borderColor: "#e2e8f0",
    marginBottom: 16,
  },
  button: {
    backgroundColor: "#3b82f6",
    padding: 16,
    borderRadius: 12,
    alignItems: "center",
    marginTop: 8,
  },
  buttonDisabled: {
    backgroundColor: "#93c5fd",
  },
  buttonText: {
    color: "white",
    fontSize: 16,
    fontWeight: "600",
  },
  link: {
    color: "#3b82f6",
    fontSize: 15,
    textAlign: "center",
    marginTop: 24,
  },
});

// Nawigacja
const Stack = createNativeStackNavigator();

export default function App() {
  return (
    //<NavigationIndependentTree>
      <NavigationContainer>
        <Stack.Navigator screenOptions={{ headerShown: false }}>
          <Stack.Screen name="Login" component={LoginScreen} />
          <Stack.Screen name="Registration" component={RegistrationScreen} />
        </Stack.Navigator>
      </NavigationContainer>
    //</NavigationIndependentTree>
  );
}