import { Injectable } from '@angular/core';
import { User } from '../model/User.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  // BehaviorSubject pour l'utilisateur connecté
  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable(); 

  constructor(private http: HttpClient) {
    const userStr = localStorage.getItem('loggedUser');
    if (userStr) {
      this.userSubject.next(JSON.parse(userStr));
    }
  }

  register(email: string, password: string, nom?: string): Observable<any> {
    const userData: any = { email, password };
    if (nom) userData.nom = nom;
    return this.http.post(`${this.apiUrl}/register`, userData);
  }
  login(email: string, password: string): Observable<User> {
    const credentials = btoa(`${email}:${password}`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`,
      'Content-Type': 'application/json'
    });
    return this.http.post<User>(`${this.apiUrl}/login`, {}, { headers }).pipe(
      map(userData => {
        this.handleLoginSuccess(userData, email, password);
        return userData;
      })
    );
  }

  /** À appeler après un login réussi pour mettre à jour l'état */
  handleLoginSuccess(user: User, email: string, password: string): void {
    this.userSubject.next(user);
    localStorage.setItem('isAuthenticated', 'true');
    localStorage.setItem('loggedUser', JSON.stringify(user));
    localStorage.setItem('authCredentials', btoa(`${email}:${password}`));
  }

  /** Déconnexion de l'utilisateur */
  logout(): void {
    this.userSubject.next(null);
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('loggedUser');
    localStorage.removeItem('authCredentials');
  }

  /** Vérifie si l'utilisateur est authentifié */
  isUserAuthenticated(): boolean {
    return !!this.userSubject.value;
  }

  /** Récupère l'utilisateur connecté */
  getLoggedUser(): User | null {
    return this.userSubject.value;
  }

  /** Récupère les credentials encodés */
  getCredentials(): string | null {
    return localStorage.getItem('authCredentials');
  }
}
