import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DersComponent } from '../list/ders.component';
import { DersDetailComponent } from '../detail/ders-detail.component';
import { DersUpdateComponent } from '../update/ders-update.component';
import { DersRoutingResolveService } from './ders-routing-resolve.service';

const dersRoute: Routes = [
  {
    path: '',
    component: DersComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DersDetailComponent,
    resolve: {
      ders: DersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DersUpdateComponent,
    resolve: {
      ders: DersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DersUpdateComponent,
    resolve: {
      ders: DersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dersRoute)],
  exports: [RouterModule],
})
export class DersRoutingModule {}
