import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BolumComponent } from '../list/bolum.component';
import { BolumDetailComponent } from '../detail/bolum-detail.component';
import { BolumUpdateComponent } from '../update/bolum-update.component';
import { BolumRoutingResolveService } from './bolum-routing-resolve.service';

const bolumRoute: Routes = [
  {
    path: '',
    component: BolumComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BolumDetailComponent,
    resolve: {
      bolum: BolumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BolumUpdateComponent,
    resolve: {
      bolum: BolumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BolumUpdateComponent,
    resolve: {
      bolum: BolumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bolumRoute)],
  exports: [RouterModule],
})
export class BolumRoutingModule {}
