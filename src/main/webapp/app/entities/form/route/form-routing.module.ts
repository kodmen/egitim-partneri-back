import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormComponent } from '../list/form.component';
import { FormDetailComponent } from '../detail/form-detail.component';
import { FormUpdateComponent } from '../update/form-update.component';
import { FormRoutingResolveService } from './form-routing-resolve.service';

const formRoute: Routes = [
  {
    path: '',
    component: FormComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormDetailComponent,
    resolve: {
      form: FormRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormUpdateComponent,
    resolve: {
      form: FormRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormUpdateComponent,
    resolve: {
      form: FormRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formRoute)],
  exports: [RouterModule],
})
export class FormRoutingModule {}
