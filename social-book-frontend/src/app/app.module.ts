import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideHttpClient, withFetch, withInterceptors} from '@angular/common/http';
import { LoginComponent } from './pages/login/login.component';
import {FormsModule} from '@angular/forms';
import { RegisterComponent } from './pages/register/register.component';
import { ActivateAccountComponent } from './pages/activate-account/activate-account.component';
import {CodeInputModule} from "angular-code-input";
import {httpTokenInterceptor} from './services/interceptor/http-token.interceptor';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatPaginatorModule} from '@angular/material/paginator';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ActivateAccountComponent
  ],
  imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        CodeInputModule,
        NgbModule,
        BrowserAnimationsModule,
        MatPaginatorModule
  ],
  providers: [
    provideHttpClient(withFetch(), withInterceptors([httpTokenInterceptor]))

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
